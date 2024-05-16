package cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.entities.GitCommit;
import core.repository.GitObjectRepository;
import core.utils.HashUtils;
import infra.persistence.GitObjectInMemoryRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class CommitTreeCommandTest {

  private GitObjectRepository repo;

  @BeforeEach
  public void setup() {
    repo = new GitObjectInMemoryRepository();
  }

  @Test
  void execute() throws IOException {

    String treeSha = HashUtils.randomShaHashString();
    String commitSha = HashUtils.randomShaHashString();
    String message = "Sample commit message";

    // Act
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    String[] args = {treeSha, "-p", commitSha, "-m", message};
    new CommandLine(new CommitTreeCommand(repo)).execute(args);

//    CommitTreeCliCommand command = new CommitTreeCliCommand(repo);
//    command.execute(args);

    // Assert
    String shaHash = outContent.toString().trim();
    assertNotNull(shaHash);

    GitCommit commit = (GitCommit) repo.findByHash(shaHash);
    assertNotNull(commit);

    String content = new String(commit.toByteArray());
    String[] parts = content.split("\0");
    assertTrue(parts[1].endsWith("\n"));
    assertTrue(parts[0].startsWith("commit "));

    parts = parts[1].split("\n");
    assertEquals("tree " + treeSha, parts[0]);
    assertEquals("parent " + commitSha, parts[1]);
    // This is hardcoded in the program
    assertTrue(parts[2].startsWith("author Marco <marco@marcolenzo.eu>"));
    assertEquals("", parts[3]);
    assertEquals(message, parts[4]);
  }

}