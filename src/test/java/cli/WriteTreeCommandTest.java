package cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import core.repository.GitObjectRepository;
import core.utils.HashUtils;
import infra.persistence.GitObjectInMemoryRepository;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class WriteTreeCommandTest {

  private GitObjectRepository repo;
  private String basePath;

  @BeforeEach
  public void setup() {
    this.basePath = "/tmp/" + UUID.randomUUID() + "/";
    repo = new GitObjectInMemoryRepository();
//    repo.setBasePath(basePath);
//    useCase = new WriteTreeUseCase(repo);
  }

  @Test
  void execute() throws IOException {
    Files.createDirectory(Path.of(basePath));

    String content1 = "file1";
    String name1 = "file1.txt";
    String shaHash1 = HashUtils.hashInHexFormat(("blob 5\0" + content1).getBytes());
    File f1 = new File(basePath + name1);
    f1.deleteOnExit();
    Files.write(f1.toPath(), content1.getBytes());

    // Act
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    new CommandLine(new WriteTreeCommand(repo, Path.of(basePath))).execute(new String[]{});
//    WriteTreeCliCommand command = new WriteTreeCliCommand(repo, Path.of(basePath));
//    command.execute(args);

    // Assert
    String expectedHash = "74cd591309a2215f600a1ee3b784152659ea1cfe";
    assertEquals(expectedHash, outContent.toString());
    assertNotNull(repo.findByHash(expectedHash));
  }

}