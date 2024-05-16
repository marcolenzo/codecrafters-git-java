package core.usecases;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import core.entities.GitBlob;
import core.utils.ByteArrayUtils;
import core.utils.HashUtils;
import infra.persistence.GitObjectInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashObjectUseCaseTest {

  private HashObjectUseCase useCase;
  private GitObjectInMemoryRepository repo;

  @BeforeEach
  void setUp() {
    repo = new GitObjectInMemoryRepository();
    useCase = new HashObjectUseCase(repo);
  }

  @Test
  void hashObject() {
    String blob = "Test content";
    byte[] expectedByteArray = ByteArrayUtils.concat("blob ".getBytes(),
        String.valueOf(blob.getBytes().length).getBytes(), new byte[]{0}, blob.getBytes());
    String expectedShaHash = HashUtils.hashInHexFormat(expectedByteArray);

    String shaHash = useCase.hashObject(blob.getBytes());
    assertEquals(expectedShaHash, shaHash);

    GitBlob gitBlob = (GitBlob) repo.findByHash(shaHash);
    assertNotNull(gitBlob);
    assertArrayEquals(expectedByteArray, gitBlob.toByteArray());
    assertEquals(blob, new String(gitBlob.getBody()));
  }

}