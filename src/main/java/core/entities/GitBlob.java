package core.entities;

import core.enums.GitObjectType;

public class GitBlob extends GitObject {

  public GitBlob(byte[] rawBody) {
    super(GitObjectType.BLOB, rawBody);
  }

}
