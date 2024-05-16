package cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.repository.GitObjectRepository;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import infra.persistence.GitObjectInMemoryRepository;
import picocli.CommandLine;

public class HashObjectCommandTest {

  private GitObjectRepository repo;

  @BeforeEach
  public void setup() {
    repo = new GitObjectInMemoryRepository();
  }

  @Test
  void execute() throws IOException {
    // Arrange
    String content = "hello world";
    String path = "test.txt";
    File f = new File(path);
    f.createNewFile();
    Files.write(f.toPath(), content.getBytes());

    // Run the command
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    String[] args = {"-w", path};
    new CommandLine(new HashObjectCommand(repo)).execute(args);

    // Assert that the output is the expected hash
    assertEquals("95d09f2b10159347eece71399a7e2e907ea3df4f", outContent.toString().trim());
  }
}
