package cli;

import core.repository.GitObjectRepository;
import core.usecases.HashObjectUseCase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import infra.persistence.GitObjectFileRepository;
import picocli.CommandLine;

@CommandLine.Command(name = "hash-object")
public class HashObjectCommand implements Runnable {

  private final HashObjectUseCase useCase;
  @CommandLine.Option(names = "-w")
  private boolean write;

  @CommandLine.Parameters
  private String filePath;

  public HashObjectCommand() {
    this(new GitObjectFileRepository());
  }

  public HashObjectCommand(GitObjectRepository repo) {
    this.useCase = new HashObjectUseCase(repo);
  }

//  public void execute(String[] args) throws IOException {
//    validate(args);
//    var filePath = args[2];
//    System.out.print(useCase.hashObject(
//        Files.readAllBytes(new File(filePath).toPath())));
//  }
//
//  private void validate(String[] args) {
//    if (args.length != 3 && !args[1].equals("-w")) {
//      throw new IllegalArgumentException("Usage: hash-object -w <path to file>");
//    }
//  }

  @Override
  public void run() {
    try {
      System.out.print(useCase.hashObject(
          Files.readAllBytes(new File(filePath).toPath())));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
