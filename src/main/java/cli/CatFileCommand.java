package cli;

import core.entities.GitObject;
import core.repository.GitObjectRepository;
import core.usecases.CatFileUseCase;
import infra.persistence.GitObjectFileRepository;
import picocli.CommandLine;

@CommandLine.Command(name = "cat-file")
public class CatFileCommand implements Runnable {

  private final CatFileUseCase useCase;

  @CommandLine.Option(names = "-p")
  private boolean print;

  @CommandLine.Parameters
  private String hash;

  public CatFileCommand() {
    this(new GitObjectFileRepository());
  }

  public CatFileCommand(GitObjectRepository repo) {
    this.useCase = new CatFileUseCase(repo);
  }

  @Override
  public void run() {
    if (print) {
      GitObject gitObject = useCase.getContent(hash);
      System.out.print(new String(gitObject.getBody()));
    }
  }
}
