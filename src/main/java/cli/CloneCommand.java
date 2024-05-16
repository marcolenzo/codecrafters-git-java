package cli;

import core.repository.GitObjectRepository;
import core.usecases.CloneRepositoryUseCase;
import infra.persistence.GitObjectFileRepository;
import infra.remote.HttpGitRemoteRepository;
import java.nio.file.Path;
import picocli.CommandLine;

@CommandLine.Command(name = "clone")
public class CloneCommand implements Runnable {

  private final CloneRepositoryUseCase useCase;

  @CommandLine.Parameters
  private String repoUrl;

  @CommandLine.Parameters
  private String targetDir;

  public CloneCommand() {
    this(new GitObjectFileRepository());
  }

  public CloneCommand(GitObjectRepository repo) {
    this.useCase = new CloneRepositoryUseCase(repo, new HttpGitRemoteRepository());
  }

  @Override
  public void run() {
    useCase.cloneRepository(repoUrl, Path.of(targetDir));
  }
}
