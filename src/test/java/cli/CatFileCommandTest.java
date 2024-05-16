package cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.entities.GitBlob;
import core.repository.GitObjectRepository;
import infra.persistence.GitObjectInMemoryRepository;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class CatFileCommandTest {

  private GitObjectRepository repo;

  @BeforeEach
  public void setup() {
    repo = new GitObjectInMemoryRepository();
  }

  @Test
  void execute() throws Exception {
    String content = "Test content";
    GitBlob blob = new GitBlob(content.getBytes());

    String hash = repo.save(blob);

    // Act
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    String[] args = {"-p", hash};
    new CommandLine(new CatFileCommand(repo)).execute(args);

    // Assert
    assertEquals(content, outContent.toString().trim());
  }

}