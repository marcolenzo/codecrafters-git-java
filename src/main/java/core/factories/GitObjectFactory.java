package core.factories;

import core.entities.GitBlob;
import core.entities.GitCommit;
import core.entities.GitObject;
import core.entities.GitTree;
import core.enums.GitObjectType;
import core.utils.ByteArrayUtils;
import java.util.Arrays;

/**
 * Factory class that generates the correct GitObject type from raw bytes.
 */
public class GitObjectFactory {

  /**
   * @param raw The raw bytes including the header
   * @return
   */
  public static GitObject createGitObjectFromRawBytes(byte[] raw) {
    int nullIndex = ByteArrayUtils.findFirstNullByte(0, raw);

    String[] headerParts = new String(Arrays.copyOfRange(raw, 0, nullIndex)).split(" ");
    GitObjectType type = GitObjectType.fromType(headerParts[0]);
    int size = Integer.parseInt(headerParts[1]);

    byte[] body = Arrays.copyOfRange(raw, nullIndex + 1, raw.length);
    if (body.length != size) {
      throw new RuntimeException("Invalid body size. Expected " + size + " got " + body.length);
    }

    return switch (type) {
      case BLOB -> new GitBlob(body);
      case TREE -> new GitTree(body);
      case COMMIT -> new GitCommit(body);
      default -> throw new RuntimeException("Invalid type");
    };
  }

}
