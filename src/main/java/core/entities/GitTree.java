package core.entities;

import core.entities.vos.GitTreeEntry;
import core.enums.GitObjectType;
import core.utils.ByteArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitTree extends GitObject {

  private List<GitTreeEntry> entries;

  public GitTree(byte[] body) {
    super(GitObjectType.TREE, body);
    this.entries = new ArrayList<>();
    parseBody();
  }

  public GitTree(List<GitTreeEntry> entries) {
    super(GitObjectType.TREE, GitTree.serializeEntries(entries));
    this.entries = entries;
  }

  public static byte[] serializeEntries(List<GitTreeEntry> entries) {
    return ByteArrayUtils.concat(
        entries.stream().map(GitTreeEntry::toByteArray).toArray(byte[][]::new));
  }

  /**
   * Populates a list of {@link GitTreeEntry} from the raw {@link GitObject} body.
   */
  public void parseBody() {
    int currentIndex = 0;
    int nullIndex = 0;
    while ((nullIndex = ByteArrayUtils.findFirstNullByte(currentIndex, getBody())) != -1) {
      entries.add(GitTreeEntry.buildGitTreeEntry(
          Arrays.copyOfRange(getBody(), currentIndex, nullIndex + 21)));
      currentIndex = nullIndex + 21;
    }
  }

  public List<GitTreeEntry> getEntries() {
    return entries;
  }

  @Override
  public String toString() {
    return "GitTree{" +
        "entries=" + entries +
        '}';
  }
}


