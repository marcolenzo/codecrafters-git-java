package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.entities.GitBlob;
import core.repository.GitObjectRepository;
import core.utils.HashUtils;
import infra.persistence.GitObjectFileRepository;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GitObjectFileRepositoryTest {

  private final String basePath = "/tmp/";
  private GitObjectFileRepository repo;

  @BeforeEach
  void setUp() {
    repo = new GitObjectFileRepository();
    repo.setBasePath(basePath);
  }

  @Test
  void findByHash() throws IOException {
    String content = "Test content";
    GitBlob blob = new GitBlob(content.getBytes());
    String shaHash = repo.save(blob);
    GitBlob retrievedBlob = (GitBlob) repo.findByHash(shaHash);
    assertEquals(content, new String(retrievedBlob.getBody()));
  }

  @Test
  void save() {
    String content = "Test content";
    GitBlob blob = new GitBlob(content.getBytes());
    String expectedShaHash = HashUtils.hashInHexFormat(blob.toByteArray());
    String shaHash = repo.save(blob);
    // verify shaHash
    assertEquals(expectedShaHash, shaHash);

    // verify file exists
    String expectedFilePath =
        basePath + GitObjectRepository.DEFAULT_PATH + "/" + expectedShaHash.substring(0, 2) + "/"
            + expectedShaHash.substring(2);
    File expectedFile = new File(expectedFilePath);
    assertTrue(expectedFile.exists());
  }

}