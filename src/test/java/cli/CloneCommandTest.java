package cli;

import core.repository.GitObjectRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import infra.persistence.GitObjectInMemoryRepository;
import picocli.CommandLine;

class CloneCommandTest {

  private GitObjectRepository repo;

  @BeforeEach
  public void setup() {
    repo = new GitObjectInMemoryRepository();
  }

  @Test
  void cloneCommand() throws Exception {
    // Arrange
    String repoUrl = "https://github.com/marcolenzo/oop-design-patterns";
    String targetDir = "/tmp/cloneCommandTests/" + UUID.randomUUID();
    new CommandLine(new CloneCommand(repo)).execute(new String[]{repoUrl, targetDir});

    //TODO some assertion
  }

}