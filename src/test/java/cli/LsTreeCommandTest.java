package cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.entities.GitObject;
import core.factories.GitObjectFactory;
import core.repository.GitObjectRepository;
import core.utils.ByteArrayUtils;
import core.utils.HashUtils;
import infra.persistence.GitObjectInMemoryRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class LsTreeCommandTest {

  private GitObjectRepository repo;

  @BeforeEach
  public void setup() {
    repo = new GitObjectInMemoryRepository();
  }

  @Test
  void execute() throws IOException {
    // Arrange
    byte[] rawContent = ByteArrayUtils.concat("tree 98\0".getBytes(),
        "40000 dir1 \0".getBytes(), HashUtils.randomShaHash(),
        "40000 dir2 \0".getBytes(), HashUtils.randomShaHash(),
        "100644 file1 \0".getBytes(), HashUtils.randomShaHash());

    GitObject tree = GitObjectFactory.createGitObjectFromRawBytes(rawContent);

    String shaHash = repo.save(tree);

    // Act
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    String[] args = {"--name-only", shaHash};
    new CommandLine(new LsTreeCommand(repo)).execute(args);

    // Assert
    String expectedContent = """
        dir1
        dir2
        file1
        """;
    assertEquals(expectedContent, outContent.toString());
  }

}