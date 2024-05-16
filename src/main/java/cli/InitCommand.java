package cli;

import core.usecases.InitUseCase;
import picocli.CommandLine;

@CommandLine.Command(name = "init")
public class InitCommand implements Runnable {

  private final InitUseCase initUseCase;

  public InitCommand() {
    this.initUseCase = new InitUseCase();
  }

  @Override
  public void run() {
    initUseCase.init("");
  }
}
