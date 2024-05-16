package core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class HashUtilsTest {

  @Test
  void hashInHexFormat() throws IOException {
    // Create file
    String content = "hello world";
    File f = new File("test.txt");
    f.deleteOnExit();
    // write content to file
    Files.write(f.toPath(), content.getBytes());

    // Act
    String hash = HashUtils.hashInHexFormat(Files.readAllBytes(f.toPath()));

    // Assert
    assertEquals("2aae6c35c94fcfb415dbe95f408b9ce91ee846ed", hash);
  }
}