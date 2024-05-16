package core.entities;

import com.google.common.primitives.Bytes;
import core.enums.GitObjectType;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GitCommit extends GitObject {

  private final List<String> parents;
  private final List<String> authors;
  private final List<String> committers;
  private String treeSha;
  private String message;

  public GitCommit(byte[] body) {
    super(GitObjectType.COMMIT, body);
    this.parents = new ArrayList<>();
    this.authors = new ArrayList<>();
    this.committers = new ArrayList<>();
    parseBody();
  }

  public static byte[] serialize(String treeSha, List<String> parentsSha, List<String> authors,
      List<String> committers, String message) {
    byte[] treeShaBytes = Bytes.concat("tree ".getBytes(), treeSha.getBytes(),
        "\n".getBytes());

    byte[] parentsBytes = new byte[0];
    for (String parent : parentsSha) {
      parentsBytes = Bytes.concat(parentsBytes, "parent ".getBytes(), parent.getBytes(),
          "\n".getBytes());
    }

    byte[] authorsBytes = new byte[0];
    for (String author : authors) {
      authorsBytes = Bytes.concat(authorsBytes, "author ".getBytes(), author.getBytes(),
          "\n".getBytes());
    }

    byte[] committersBytes = new byte[0];
    for (String committer : committers) {
      committersBytes = Bytes.concat(authorsBytes, "committer ".getBytes(), committer.getBytes(),
          "\n".getBytes());
    }

    return Bytes.concat(treeShaBytes, parentsBytes, authorsBytes, committersBytes,
        "\n".getBytes(), message.getBytes(), "\n".getBytes());
  }

  /**
   * Populates the commit information from the raw {@link GitObject} body.
   */
  public void parseBody() {
    ByteArrayInputStream bais = new ByteArrayInputStream(getBody());
    BufferedReader reader = new BufferedReader(new InputStreamReader(bais));

    this.treeSha = "";
    this.message = "";

    try {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("tree ")) {
          this.treeSha = line.split(" ")[1];
        } else if (line.startsWith("parent ")) {
          this.parents.add(line.split(" ")[1]);
        } else if (line.startsWith("author ")) {
          this.authors.add(line.replaceFirst("author ", ""));
        } else if (line.startsWith("committer ")) {
          this.committers.add(line.replaceFirst("author ", ""));
        } else {
          this.message = this.message.concat(line);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<String> getParents() {
    return parents;
  }

  public List<String> getAuthors() {
    return authors;
  }

  public List<String> getCommitters() {
    return committers;
  }

  public String getTreeSha() {
    return treeSha;
  }

  public String getMessage() {
    return message;
  }
}
