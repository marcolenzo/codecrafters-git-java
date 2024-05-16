package core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Random;

public class HashUtils {

  public static byte[] randomShaHash() {
    Random random = new Random();
    byte[] randomBytes = new byte[20];
    random.nextBytes(randomBytes);

    return shaHash(randomBytes);
  }

  public static String randomShaHashString() {
    return HexFormat.of().formatHex(randomShaHash());
  }


  public static byte[] shaHash(byte[] content) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      return md.digest(content);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String hashInHexFormat(byte[] content) {
    return HexFormat.of().formatHex(shaHash(content));
  }

  public static byte[] convertHexToBytes(String hexString) {
    int length = hexString.length();
    if (length % 2 != 0) {
      throw new IllegalArgumentException("Hex string length must be even");
    }

    byte[] bytes = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
      String hexByte = hexString.substring(i, i + 2);
      bytes[i / 2] = (byte) Integer.parseInt(hexByte, 16);
    }

    return bytes;
  }

}
