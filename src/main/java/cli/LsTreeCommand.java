package cli;

import core.entities.GitTree;
import core.repository.GitObjectRepository;
import core.usecases.LsTreeUseCase;
import infra.persistence.GitObjectFileRepository;
import java.io.IOException;
import picocli.CommandLine;

@CommandLine.Command(name = "ls-tree")
public class LsTreeCommand implements Runnable {

  private final LsTreeUseCase useCase;

  @CommandLine.Option(names = "--name-only")
  private boolean printNameOnly;

  @CommandLine.Parameters
  private String hash;

  public LsTreeCommand() {
    this(new GitObjectFileRepository());
  }

  public LsTreeCommand(GitObjectRepository repo) {
    this.useCase = new LsTreeUseCase(repo);
  }

//  @Override
//  public void execute(String[] args) throws IOException {
//    validate(args);
//    // args[2] should be the object's hash
//    GitTree gitTree = useCase.lsTree(args[2]);
//    gitTree.getEntries().forEach(e -> System.out.println(e.getName()));
//  }
//
//  private void validate(String[] args) {
//    if (args.length != 3 && !args[1].equals("--name-only")) {
//      throw new IllegalArgumentException("Usage: ls-tree --name-only <tree_sha>");
//    }
//  }

  @Override
  public void run() {
    try {
      GitTree gitTree = useCase.lsTree(hash);

      if (printNameOnly) {
        gitTree.getEntries().forEach(e -> System.out.println(e.name()));
      } else {
        gitTree.getEntries().forEach(e -> System.out.println(e.name()));
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
