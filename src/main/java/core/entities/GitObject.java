package core.entities;

import com.google.common.primitives.Bytes;
import core.enums.GitObjectType;
import core.utils.ByteArrayUtils;
import java.util.Arrays;

/**
 * A GitObject is a wrapper around a byte array that contains the header (type and size). Body and
 * header are separated by a NULL byte.
 */
public abstract class GitObject {

  private static final byte[] NULL_BYTE = new byte[]{0};
  private final GitObjectType type;
  private final byte[] body;
  private final int size;


  public GitObject(GitObjectType type, byte[] body) {
    this.type = type;
    this.body = body;
    this.size = body.length;
  }

  public static GitObjectType parseType(byte[] raw) {
    int nullIndex = ByteArrayUtils.findFirstNullByte(0, raw);
    String[] headerParts = new String(Arrays.copyOfRange(raw, 0, nullIndex)).split(" ");
    return GitObjectType.fromType(headerParts[0]);
  }

  protected byte[] headerToByteArray() {
    return Bytes.concat(type.getType().getBytes(), " ".getBytes(), String.valueOf(size).getBytes());
  }

  public GitObjectType getType() {
    return type;
  }

  public int getSize() {
    return size;
  }

  public byte[] getBody() {
    return body;
  }


  public final byte[] toByteArray() {
    return Bytes.concat(headerToByteArray(), NULL_BYTE, body);
  }

//  public abstract void parseBody();


}
