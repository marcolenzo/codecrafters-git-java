package core.enums;

public enum GitPackObjectType {

  COMMIT(1, "commit"),
  TREE(2, "tree"),
  BLOB(3, "blob"),
  TAG(4, "tag"),
  OFS_DELTA(6, "ofs-delta"),
  REF_DELTA(7, "ref-delta");

  private final int type;
  private final String typeName;

  GitPackObjectType(int type, String typeName) {
    this.type = type;
    this.typeName = typeName;
  }

  public static GitPackObjectType fromType(int type) {
    for (GitPackObjectType gitPackObjectType : GitPackObjectType.values()) {
      if (gitPackObjectType.getType() == type) {
        return gitPackObjectType;
      }
    }
    throw new IllegalArgumentException();
  }

  public int getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }
}
