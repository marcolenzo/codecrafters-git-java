package core.usecases;

import com.google.common.primitives.Bytes;
import core.entities.GitBlob;
import core.entities.GitCommit;
import core.entities.GitObject;
import core.entities.GitPack;
import core.entities.GitTree;
import core.entities.vos.GitPackObject;
import core.entities.vos.GitReferences;
import core.entities.vos.GitTreeEntry;
import core.enums.GitTreeEntryMode;
import core.factories.GitObjectFactory;
import core.repository.GitObjectRepository;
import core.repository.GitRemoteRepository;
import core.utils.HashUtils;
import infra.persistence.GitObjectFileRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CloneRepositoryUseCase {

  private final GitObjectRepository repo;
  private final GitRemoteRepository remoteRepo;

  public CloneRepositoryUseCase(GitObjectRepository repo, GitRemoteRepository remoteRepo) {
    this.repo = repo;
    this.remoteRepo = remoteRepo;
  }

  public void cloneRepository(String repoUrl, Path targetFolder) {

    if (repo instanceof GitObjectFileRepository) {
      ((GitObjectFileRepository) repo).setBasePath(targetFolder.toString());
    }

    // Get refs
    GitReferences gitReferences = remoteRepo.getReferences(repoUrl);

    // Get pack
    GitPack pack = remoteRepo.getPack(repoUrl, gitReferences.refs());

    // Initialize Repo
    InitUseCase initUseCase = new InitUseCase();
    initUseCase.init(targetFolder.toString() + File.separator);

    // Write Objects
    writeObjects(pack, targetFolder);

    // Write files
    try {
      GitCommit commit = (GitCommit) repo.findByHash(gitReferences.headHash());
      GitTree tree = (GitTree) repo.findByHash(commit.getTreeSha());
      checkOut(tree, targetFolder);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private void checkOut(GitTree tree, Path targetFolder) throws IOException {
    for (GitTreeEntry entry : tree.getEntries()) {
      GitObject gitObject = repo.findByHash(entry.shaHash());

      switch (entry.mode()) {
        case GitTreeEntryMode.DIRECTORY:
          GitTree subTree = (GitTree) gitObject;
          Path treeFolder = Path.of(targetFolder.toString(), entry.name());
          Files.createDirectories(treeFolder);
          checkOut(subTree, treeFolder);
          break;
        case GitTreeEntryMode.EXECUTABLE:
        case GitTreeEntryMode.REGULAR:
          GitBlob blob = (GitBlob) gitObject;
          Path targetPath = Path.of(targetFolder.toString(), entry.name());
          Files.createDirectories(targetPath.getParent());
          Files.write(targetPath, blob.getBody());
          break;
      }
    }
  }

  public void writeObjects(GitPack pack, Path targetFolder) {

    System.out.println("--- Writing objects ---");
    System.out.println("Total objects " + pack.getPackObjects().size());
    System.out.println("Total undeltified objects " + pack.getPackObjects().stream()
        .filter(GitPackObject::isNotDeltified).count());
    System.out.println("Total deltified objects " + pack.getPackObjects().stream()
        .filter(GitPackObject::isDeltified).count());

    // Write undeltified objects
    pack.getPackObjects().stream().filter(GitPackObject::isNotDeltified).forEach(
        gitPackObject -> {
          byte[] content = gitPackObject.getContent();
          byte[] formattedContent =
              Bytes.concat(gitPackObject.getType().getTypeName().getBytes(),
                  " ".getBytes(),
                  String.valueOf(gitPackObject.getSize()).getBytes(), new byte[]{0}, content);
          GitObject gitObject = GitObjectFactory.createGitObjectFromRawBytes(formattedContent);
          String expectedHash = HashUtils.hashInHexFormat(formattedContent);
          String generatedHash = repo.save(gitObject);
          assert expectedHash.equals(generatedHash);
        }

    );

    // Write deltified objects
    pack.getPackObjects().stream().filter(GitPackObject::isDeltified).forEach(
        gitPackObject -> {
          byte[] sourceContent = null;
          GitObject sourceObject = repo.findByHash(gitPackObject.getBaseShaHash());
          byte[] deltifiedContent = gitPackObject.writeDeltifiedObject(sourceObject,
              targetFolder);
          byte[] formattedContent = Bytes.concat(
              sourceObject.getType().getType().getBytes(),
              " ".getBytes(),
              Integer.toString(deltifiedContent.length).getBytes(), new byte[]{0},
              deltifiedContent);
          GitObject deltifiedGitObject = GitObjectFactory.createGitObjectFromRawBytes(
              formattedContent
          );
          String generatedHash = repo.save(deltifiedGitObject);
        }
    );


  }
}
