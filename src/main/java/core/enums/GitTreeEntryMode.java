package core.enums;

import java.nio.file.Files;
import java.nio.file.Path;

public enum GitTreeEntryMode {
  REGULAR("100644"),
  EXECUTABLE("100755"),
  SYMLINK("120000"),
  DIRECTORY("40000");

  private final String mode;

  GitTreeEntryMode(String mode) {
    this.mode = mode;
  }

  public static GitTreeEntryMode fromMode(String mode) {
    for (GitTreeEntryMode gitTreeEntryMode : GitTreeEntryMode.values()) {
      if (gitTreeEntryMode.getMode().equals(mode)) {
        return gitTreeEntryMode;
      }
    }
    throw new IllegalArgumentException("Unknown mode: " + mode);
  }

  public static GitTreeEntryMode fromPath(Path path) {
    if (Files.isDirectory(path)) {
      return GitTreeEntryMode.DIRECTORY;
    } else if (Files.isExecutable(path)) {
      return GitTreeEntryMode.EXECUTABLE;
    } else if (Files.isRegularFile(path)) {
      return GitTreeEntryMode.REGULAR;
    } else if (Files.isSymbolicLink(path)) {
      return GitTreeEntryMode.SYMLINK;
    }

    throw new IllegalArgumentException("Cannot determine mode: " + path);
  }

  public String getMode() {
    return mode;
  }


}
