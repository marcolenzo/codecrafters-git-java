package core.enums;

public enum GitObjectType {

  COMMIT("commit"),
  TREE("tree"),
  BLOB("blob");

  private final String type;

  GitObjectType(String type) {
    this.type = type;
  }

  public static GitObjectType fromType(String type) {
    for (GitObjectType gitObjectType : GitObjectType.values()) {
      if (gitObjectType.getType().equals(type)) {
        return gitObjectType;
      }
    }
    throw new IllegalArgumentException();
  }

  public String getType() {
    return type;
  }

}
