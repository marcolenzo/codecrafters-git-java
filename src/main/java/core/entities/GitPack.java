package core.entities;

import com.google.common.primitives.Bytes;
import core.entities.vos.DeltaInstruction;
import core.entities.vos.GitPackObject;
import core.enums.GitPackObjectType;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The format of a Git pack is described here:
 * https://github.com/git/git/blob/master/Documentation/gitformat-pack.txt
 */
public class GitPack {

  // first bit
  public static final int PACK_MSB_MASK = 0b10000000;
  // 3 bits after MSB
  public static final int PACK_TYPE_MASK = 0b01110000;
  // last 4 bits
  public static final int PACK_4_BIT_MASK = 0b00001111;
  // 7 bits after MSB
  public static final int PACK_7_BIT_MASK = 0b01111111;
  private final int packVersion;
  private final List<GitPackObject> packObjects;

  public GitPack(int packVersion, List<GitPackObject> packObjects) {
    this.packVersion = packVersion;
    this.packObjects = packObjects;
  }

  // factory method
  public static GitPack fromInputStream(byte[] packContent) throws IOException {
    // Find signature
    int index = Bytes.indexOf(packContent, "PACK".getBytes());

    // Start parsing
    ByteBuffer buffer = ByteBuffer.wrap(
        Arrays.copyOfRange(packContent, index + 4, packContent.length));
    int packVersion = buffer.getInt();
    System.out.println("pack version: " + packVersion);
    int packObjectsCount = buffer.getInt();
    System.out.println("pack objects count: " + packObjectsCount);

    List<GitPackObject> packObjects = new ArrayList<>(packObjectsCount);
    for (int i = 0; i < packObjectsCount; i++) {
      packObjects.add(extractPackObject(buffer));
    }
    assert packObjects.size() == packObjectsCount;
    return new GitPack(packVersion, packObjects);
  }

  private static GitPackObject extractPackObject(ByteBuffer buffer) {
    // extract object type and size
    int first = Byte.toUnsignedInt(buffer.get());
    GitPackObjectType objectType = GitPackObjectType.fromType((first & PACK_TYPE_MASK) >> 4);

    int objectSize = first & PACK_4_BIT_MASK;
    int continue_read = first & PACK_MSB_MASK;
    if (continue_read > 0) {
      objectSize += (parseVariableLengthIntLittleEndian(buffer) << 4);
    }
    switch (objectType) {
      case COMMIT, TREE, BLOB -> {
        return new GitPackObject(objectType, objectSize,
            getInflated(buffer, objectSize));
      }
      case REF_DELTA -> {
        byte[] hash = new byte[20];
        buffer.get(hash);
        String baseShaHash = HexFormat.of().formatHex(hash);

        ByteBuffer deltaBuffer = ByteBuffer.wrap(getInflated(buffer, objectSize));

        // parse source and target data length.
        int sourceSize = parseVariableLengthIntLittleEndian(deltaBuffer);
        int targetSize = parseVariableLengthIntLittleEndian(deltaBuffer);

        return new GitPackObject(objectType, targetSize, sourceSize, baseShaHash,
            DeltaInstruction.fromBuffer(deltaBuffer));
      }
      default -> System.out.println("Unsupported (for now) object type: " + objectType);
    }
    throw new RuntimeException("Unsupported (for now) object type: " + objectType);
  }

  private static int parseVariableLengthIntLittleEndian(ByteBuffer pack) {
    int val = 0;
    int shift = 0;
    int continue_read = 1;

    while (continue_read > 0) {
      int first = Byte.toUnsignedInt(pack.get());
      val = val + ((first & PACK_7_BIT_MASK) << shift);
      continue_read = first & PACK_MSB_MASK;
      shift += 7;
    }

    return val;
  }

  private static byte[] getInflated(ByteBuffer buffer, int object_size) {
    try {
      byte[] inflated = new byte[object_size];
      Inflater inflater = new Inflater();
      inflater.setInput(buffer);
      inflater.inflate(inflated);
      return inflated;
    } catch (DataFormatException e) {
      throw new RuntimeException(e);
    }
  }

  public int getPackVersion() {
    return packVersion;
  }

  public List<GitPackObject> getPackObjects() {
    return packObjects;
  }


}
