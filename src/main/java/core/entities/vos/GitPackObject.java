package core.entities.vos;

import core.entities.GitObject;
import core.enums.DeltaType;
import core.enums.GitPackObjectType;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GitPackObject {

  private final GitPackObjectType type;
  private final int size;
  private final int sourceSize;
  private final byte[] content;
  private final String baseShaHash;
  private final boolean deltified;
  private final List<DeltaInstruction> deltaInstructions;

  public GitPackObject(GitPackObjectType type, int size, byte[] content) {
    this.type = type;
    this.size = size;
    this.sourceSize = size;
    this.content = content;
    this.deltified = false;
    this.deltaInstructions = new ArrayList<>();
    this.baseShaHash = null;
    assert size == content.length;
  }

  public GitPackObject(GitPackObjectType type, int size, int sourceSize, String baseShaHash,
      List<DeltaInstruction> deltaInstructions) {
    this.type = type;
    this.deltified = true;
    this.deltaInstructions = deltaInstructions;
    this.content = new byte[0];
    this.size = size;
    this.sourceSize = sourceSize;
    this.baseShaHash = baseShaHash;
  }

  public String getBaseShaHash() {
    return baseShaHash;
  }

  public GitPackObjectType getType() {
    return type;
  }

  public int getSize() {
    return size;
  }

  public byte[] getContent() {
    return content;
  }

  public boolean isDeltified() {
    return deltified;
  }

  public boolean isNotDeltified() {
    return !deltified;
  }

  public int getSourceSize() {
    return sourceSize;
  }

  public List<DeltaInstruction> getDeltaInstructions() {
    return deltaInstructions;
  }


  public byte[] writeDeltifiedObject(GitObject gitObject, Path targetFolder) {

    byte[] sourceContent = gitObject.getBody();

    byte[] content = new byte[this.size];
    ByteBuffer buffer = ByteBuffer.wrap(content);

    for (DeltaInstruction deltaInstruction : this.deltaInstructions) {
      if (deltaInstruction.type() == DeltaType.COPY) {
        buffer.put(sourceContent, deltaInstruction.offset(),
            deltaInstruction.size());
      } else if (deltaInstruction.type() == DeltaType.INSERT) {
        buffer.put(deltaInstruction.data());
      }
    }

    if (buffer.hasRemaining()) {
      throw new IllegalStateException("buffer is not full");
    }

    return content;

  }

}
