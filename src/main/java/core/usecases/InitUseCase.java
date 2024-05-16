package core.usecases;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InitUseCase {

  public void init(String baseDir) {
    System.out.println("Executing init command");
    final File root = new File(baseDir + ".git");
    new File(root, "objects").mkdirs();
    new File(root, "refs").mkdirs();
    final File head = new File(root, "HEAD");

    try {
      head.createNewFile();
      Files.write(head.toPath(), "ref: refs/heads/main\n".getBytes());
      System.out.println("Initialized git directory");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
