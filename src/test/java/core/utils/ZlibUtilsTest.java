package core.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class ZlibUtilsTest {

  @Test
  public void testCompressionDecompression() {
    ZlibUtils zlibUtils = new ZlibUtils();
    String testString = "This is a test string";
    byte[] testData = testString.getBytes(StandardCharsets.UTF_8);

    // Compress the data
    byte[] compressedData = zlibUtils.compressBlob(testData);
    assertTrue(compressedData.length > 0);

    // Decompress the data
    byte[] decompressedData = zlibUtils.decompressBlob(compressedData);
    // Ensure the decompressed data matches the original data
    assertArrayEquals(testData, decompressedData);
  }
}