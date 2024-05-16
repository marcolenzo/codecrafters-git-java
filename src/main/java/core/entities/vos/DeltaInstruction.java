package core.entities.vos;

import core.enums.DeltaType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record DeltaInstruction(DeltaType type, byte[] data, int offset, int size) {

  public static final int PACK_7_BIT_MASK = 0b01111111;
  public static final int PACK_MSB_MASK = 0b10000000;


  public static List<DeltaInstruction> fromBuffer(ByteBuffer buffer) {
    List<DeltaInstruction> deltaInstructions = new ArrayList<>();
    while (buffer.hasRemaining()) {
      int read = buffer.get();
      int type = read & PACK_MSB_MASK;
      DeltaType deltaType;
      byte[] data = new byte[0];
      int offset = 0;
      int size = 0;

      if (type == 0) { // insert
//        DeltaInstructions.setDelta_type(DeltaType.INSERT);
        deltaType = DeltaType.INSERT;
        int deltaSize = read & PACK_7_BIT_MASK;
        data = new byte[deltaSize];
        buffer.get(data);
      } else { // copy
        deltaType = DeltaType.COPY;
//        DeltaInstructions.setDelta_type(DeltaType.COPY);
        // offset, size in little endian
        boolean offset1 = (read & 0b00000001) != 0;
        boolean offset2 = (read & 0b00000010) != 0;
        boolean offset3 = (read & 0b00000100) != 0;
        boolean offset4 = (read & 0b00001000) != 0;
        boolean size1 = (read & 0b00010000) != 0;
        boolean size2 = (read & 0b00100000) != 0;
        boolean size3 = (read & 0b01000000) != 0;

        offset = parseVariableLengthInt(buffer, offset1, offset2, offset3, offset4);
        size = parseVariableLengthInt(buffer, size1, size2, size3);
        if (size == 0) {
          size = 0x10000;
        }
      }
      deltaInstructions.add(new DeltaInstruction(deltaType, data, offset, size));
    }
    return deltaInstructions;
  }

  private static int parseVariableLengthInt(ByteBuffer buffer, boolean... flags) {
    int val = 0;
    int shift = 0;

    for (boolean flag : flags) {
      if (flag) {
        val += ((Byte.toUnsignedInt(buffer.get())) << shift);
      }
      shift += 8;
    }

    return val;
  }

}
