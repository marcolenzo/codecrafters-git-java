package cli;

import core.repository.GitObjectRepository;
import core.usecases.WriteTreeUseCase;
import java.nio.file.Path;
import infra.persistence.GitObjectFileRepository;
import picocli.CommandLine;

@CommandLine.Command(name = "write-tree")
public class WriteTreeCommand implements Runnable {

  private final WriteTreeUseCase useCase;
  private final Path workingDir;

  public WriteTreeCommand() {
    this(new GitObjectFileRepository());
  }

  public WriteTreeCommand(GitObjectRepository repo) {
    this(repo, Path.of(System.getProperty("user.dir")));
  }

  public WriteTreeCommand(GitObjectRepository repo, Path workingDir) {
    this.useCase = new WriteTreeUseCase(repo);
    this.workingDir = workingDir;
  }

  @Override
  public void run() {
    String shaHash = useCase.writeTree(workingDir);
    System.out.print(shaHash);
  }

}
