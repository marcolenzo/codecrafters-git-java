package core.usecases;

import core.entities.GitBlob;
import core.entities.GitTree;
import core.entities.vos.GitTreeEntry;
import core.enums.GitTreeEntryMode;
import core.repository.GitObjectRepository;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WriteTreeUseCase {

  private final GitObjectRepository repo;

  public WriteTreeUseCase(GitObjectRepository repo) {
    this.repo = repo;
  }

  public String writeTree(Path workingDir) {

    List<GitTreeEntry> entries = new ArrayList<>();

    // Scan directory
    List<Path> sortedPaths = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(workingDir)) {
      for (Path entry : stream) {
        sortedPaths.add(entry);
      }
      Collections.sort(sortedPaths);

      for (Path p : sortedPaths) {
        if (p.endsWith(".git")) {
          continue;
        } else if (Files.isDirectory(p)) {
          String shaHash = writeTree(p);
          entries.add(new GitTreeEntry(GitTreeEntryMode.DIRECTORY, p.getFileName().toString(),
              shaHash));
        } else {
          // Regular file
          byte[] content = Files.readAllBytes(p);
          GitBlob gitObject = new GitBlob(content);
          String shaHash = repo.save(gitObject);
          entries.add(new GitTreeEntry(GitTreeEntryMode.fromPath(p), p.getFileName().toString(),
              shaHash));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    GitTree tree = new GitTree(entries);
    return repo.save(tree);
  }

}
