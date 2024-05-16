package core.entities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class GitBlobTest {

  @Test
  public void testConstructor() {
    byte[] blob = "Hello, world!".getBytes();
    GitBlob gitBlob = new GitBlob(blob);
    assertArrayEquals(blob, gitBlob.getBody());
  }


}