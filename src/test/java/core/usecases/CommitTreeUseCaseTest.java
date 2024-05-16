package core.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.utils.HashUtils;
import infra.persistence.GitObjectFileRepository;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommitTreeUseCaseTest {

  private CommitTreeUseCase useCase;
  private GitObjectFileRepository repo;


  @BeforeEach
  void setUp() {
    repo = new GitObjectFileRepository();
    repo.setBasePath("/tmp");
    useCase = new CommitTreeUseCase(repo);
  }

  @Test
  void commitTree() throws IOException {
    // Create test input data
    String treeSha = HashUtils.randomShaHashString();
    String commitSha = HashUtils.randomShaHashString();
    String message = "Sample commit message";

    // Call the method to be tested
    String result = useCase.commitTree(treeSha, commitSha, message);

    // Assert the result or any interactions with the mock objects
    assertNotNull(result);

    String content = new String(repo.findByHash(result).toByteArray());
    assertNotNull(content);

    System.out.println(content);
    String[] parts = content.split("\0");
    assertTrue(parts[0].startsWith("commit "));

    parts = parts[1].split("\n");
    assertTrue(parts[0].startsWith("tree " + treeSha));
    assertTrue(parts[1].startsWith("parent " + commitSha));
    // This is hardcoded in the program
    assertTrue(parts[2].startsWith("author Marco <marco@marcolenzo.eu>"));
    assertTrue(parts[3].equals(""));
    assertTrue(parts[4].contains(message));
  }
}