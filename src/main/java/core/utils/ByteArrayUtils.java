package core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayUtils {

  public static byte[] NULL_BYTE = new byte[]{0};
  public static byte[] NULL_BYTE_STRING = new String(NULL_BYTE).getBytes();

  public static byte[] concat(byte[]... bytes) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      for (byte[] byteArray : bytes) {
        baos.write(byteArray);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static int findFirstNullByte(int startIndex, byte[] bytes) {
    if (startIndex >= bytes.length) {
      return -1;
    }

    for (int i = startIndex; i < bytes.length; i++) {
      if (bytes[i] == 0) {
        return i;
      }
    }
    return -1;
  }

}
