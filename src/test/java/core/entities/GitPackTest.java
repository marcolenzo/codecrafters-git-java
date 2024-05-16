package core.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.entities.vos.GitPackObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GitPackTest {

  @Test
  void fromInputStream() throws IOException {
    byte[] packContent = Files.readAllBytes(Path.of("src/test/resources/sample.pack"));
    GitPack pack = GitPack.fromInputStream(packContent);
    assertEquals(2, pack.getPackVersion());
    assertEquals(127, pack.getPackObjects().size());
    assertEquals(97, pack.getPackObjects().stream().filter(GitPackObject::isNotDeltified).count());
    assertEquals(30, pack.getPackObjects().stream().filter(GitPackObject::isDeltified).count());
  }
}