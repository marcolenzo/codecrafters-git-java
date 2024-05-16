package core.entities;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class GitTreeTest {

  @Test
  void createFromRawContent() throws IOException {
    // Arrange
//    byte[] dir1ShaHash = HashUtils.randomShaHash();
//    byte[] dir2ShaHash = HashUtils.randomShaHash();
//    byte[] file1ShaHash = HashUtils.randomShaHash();
//
//    byte[] rawContent;
//    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//      outputStream.write("tree 11\0".getBytes());
//      outputStream.write("040000 dir1 \0".getBytes());
//      outputStream.write(dir1ShaHash);
//      outputStream.write("040000 dir2 \0".getBytes());
//      outputStream.write(dir2ShaHash);
//      outputStream.write("100644 file1 \0".getBytes());
//      outputStream.write(file1ShaHash);
//      rawContent = outputStream.toByteArray();
//    }
//
//    System.out.println(new String(rawContent));
//
//    byte[] shaHash = HashUtils.shaHash(rawContent);
//
//    GitTree gitTree = GitTree.createFromRawContent(rawContent);
//
//    assertEquals(3, gitTree.getEntries().size());
//
//    GitTreeEntry firstEntry = gitTree.getEntries().getFirst();
//    assertEquals("dir1", firstEntry.getName());
//    assertTrue(Arrays.equals(dir1ShaHash, firstEntry.getShaHash()));
//    assertEquals("040000", firstEntry.getMode().getMode());
//
//    GitTreeEntry secondEntry = gitTree.getEntries().get(1);
//    assertEquals("dir2", secondEntry.getName());
//    assertEquals(dir2ShaHash, secondEntry.getShaHash());
//    assertEquals("040000", secondEntry.getMode().getMode());
//
//    GitTreeEntry thirdEntry = gitTree.getEntries().get(2);
//    assertEquals("file1", thirdEntry.getName());
//    assertEquals(file1ShaHash, thirdEntry.getShaHash());
//    assertEquals("100644", thirdEntry.getMode().getMode());
//
//    assertEquals("tree", gitTree.getType().getType());
//    assertEquals(shaHash, gitTree.getShaHash());
  }
}