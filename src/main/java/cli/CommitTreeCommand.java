package cli;

import core.repository.GitObjectRepository;
import core.usecases.CommitTreeUseCase;
import infra.persistence.GitObjectFileRepository;
import picocli.CommandLine;

@CommandLine.Command(name = "commit-tree")
public class CommitTreeCommand implements Runnable {

  private final CommitTreeUseCase useCase;

  @CommandLine.Option(names = "-p")
  private String parentCommitHash;

  @CommandLine.Option(names = "-m")
  private String message;

  @CommandLine.Parameters
  private String treeHash;

  public CommitTreeCommand() {
    this(new GitObjectFileRepository());
  }

  public CommitTreeCommand(GitObjectRepository repo) {
    this.useCase = new CommitTreeUseCase(repo);
  }

  @Override
  public void run() {
    System.out.print(useCase.commitTree(treeHash, parentCommitHash, message));
  }
}
