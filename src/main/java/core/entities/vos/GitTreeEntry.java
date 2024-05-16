package core.entities.vos;

import core.enums.GitTreeEntryMode;
import core.utils.ByteArrayUtils;
import core.utils.HashUtils;
import java.util.Arrays;
import java.util.HexFormat;

public record GitTreeEntry(GitTreeEntryMode mode, String name, String shaHash) {

  public static GitTreeEntry buildGitTreeEntry(byte[] byteArray) {
    int nullIndex = ByteArrayUtils.findFirstNullByte(0, byteArray);
    String[] modeNameParts = new String(Arrays.copyOfRange(byteArray, 0, nullIndex)).split(" ");
    String mode = modeNameParts[0];
    String name = modeNameParts[1];

    String shaHash = HexFormat.of().formatHex(
        Arrays.copyOfRange(byteArray, nullIndex + 1, byteArray.length));
    return new GitTreeEntry(GitTreeEntryMode.fromMode(mode), name,
        shaHash);
  }

  public byte[] toByteArray() {
    return ByteArrayUtils.concat(this.mode().getMode().getBytes(), " ".getBytes(),
        this.name().getBytes(), ByteArrayUtils.NULL_BYTE,
        HashUtils.convertHexToBytes(shaHash));
  }

}
