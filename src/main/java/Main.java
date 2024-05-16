import cli.CatFileCommand;
import cli.CloneCommand;
import cli.CommitTreeCommand;
import cli.HashObjectCommand;
import cli.InitCommand;
import cli.LsTreeCommand;
import cli.WriteTreeCommand;
import java.io.IOException;
import picocli.CommandLine;

@CommandLine.Command(name = "git", mixinStandardHelpOptions = true,
    subcommands = {
        InitCommand.class, CatFileCommand.class, HashObjectCommand.class,
        LsTreeCommand.class,
        WriteTreeCommand.class, CommitTreeCommand.class, CloneCommand.class
    })
public class Main {

  public static void main(String[] args) throws IOException {
    new CommandLine(new Main()).execute(args);
  }
}
