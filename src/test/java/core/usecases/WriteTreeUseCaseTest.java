package core.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.entities.GitTree;
import core.entities.vos.GitTreeEntry;
import core.enums.GitTreeEntryMode;
import core.repository.GitObjectRepository;
import core.utils.HashUtils;
import infra.persistence.GitObjectFileRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WriteTreeUseCaseTest {

  private WriteTreeUseCase useCase;
  private GitObjectFileRepository repo;
  private String basePath;

  @BeforeEach
  void setUp() {
    basePath = "/tmp/" + UUID.randomUUID() + "/";
    repo = new GitObjectFileRepository();
    repo.setBasePath(basePath);
    useCase = new WriteTreeUseCase(repo);
  }

  @Test
  void writeTree() throws IOException {
    Files.createDirectory(Path.of(basePath));

    String content1 = "file1";
    String name1 = "file1.txt";
    String shaHash1 = HashUtils.hashInHexFormat(("blob 5\0" + content1).getBytes());
    File f1 = new File(basePath + name1);
    f1.deleteOnExit();
    Files.write(f1.toPath(), content1.getBytes());

    String dir1 = "dir1/";
    File d = new File(basePath + name1);
    d.deleteOnExit();
    Files.createDirectory(Path.of(basePath + dir1));

    String content2 = "file2";
    String name2 = "file2.txt";
    String shaHash2 = HashUtils.hashInHexFormat(("blob 5\0" + content2).getBytes());
    File f2 = new File(basePath + dir1 + name2);
    f2.deleteOnExit();
    Files.write(f2.toPath(), content2.getBytes());

    // Act
    String shaHash = useCase.writeTree(Path.of(basePath));

    GitTree tree = (GitTree) repo.findByHash(shaHash);

    // Assert
    assertTrue(
        Files.exists(Path.of(
            basePath + GitObjectRepository.DEFAULT_PATH + "/" + shaHash1.substring(0, 2) + "/"
                + shaHash1.substring(2))));
    assertEquals(2, tree.getEntries().size());

    GitTreeEntry fileEntry = tree.getEntries().get(1);
    assertEquals(name1, fileEntry.name());
    assertEquals(GitTreeEntryMode.REGULAR, fileEntry.mode());
    assertEquals(shaHash1, fileEntry.shaHash());

  }
}