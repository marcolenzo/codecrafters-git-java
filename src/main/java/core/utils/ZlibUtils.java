package core.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZlibUtils {

  /**
   * Compresses the given data using the DEFLATE algorithm.
   *
   * @param data
   * @return
   */
  public static byte[] compressBlob(byte[] data) {
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    while (!deflater.finished()) {
      int count = deflater.deflate(buffer);
      outputStream.write(buffer, 0, count);
    }

    return outputStream.toByteArray();
  }

  /**
   * @param compressedData
   * @return
   */
  public static byte[] decompressBlob(byte[] compressedData) {
    Inflater inflater = new Inflater();
    inflater.setInput(compressedData);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    while (!inflater.finished()) {
      try {
        int decompressedDataLength = inflater.inflate(buffer);
        outputStream.write(buffer, 0, decompressedDataLength);
      } catch (DataFormatException e) {
        throw new RuntimeException("Data format exception while decompressing.", e);
      }
    }

    return outputStream.toByteArray();
  }


}
