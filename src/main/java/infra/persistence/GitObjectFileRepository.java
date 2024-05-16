package infra.persistence;

import core.entities.GitObject;
import core.factories.GitObjectFactory;
import core.repository.GitObjectRepository;
import core.utils.HashUtils;
import core.utils.ZlibUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Git Objects are stored in the .git/objects directory. The path is derived from the object's hash.
 * If an object had hash <code>e88f7a929cd70b0274c4ea33b209c97fa845fdbc</code> its path would be
 * <code>./git/objects/e8/8f7a929cd70b0274c4ea33b209c97fa845fdbc</code>
 */
public class GitObjectFileRepository implements GitObjectRepository {

  private String basePath = "";

  protected String getPath(String shaHash) {
    String pathTemplate = DEFAULT_PATH + "/%s/%s";
    if (!basePath.isEmpty()) {
      pathTemplate = basePath + File.separator + pathTemplate;
    }
    return String.format(pathTemplate, shaHash.substring(0, 2), shaHash.substring(2));
  }

  protected String getDirectory(String shaHash) {
    String directoryTemplate = DEFAULT_PATH + "/%s";
    if (!basePath.isEmpty()) {
      directoryTemplate = basePath + File.separator + directoryTemplate;
    }
    return String.format(directoryTemplate, shaHash.substring(0, 2));
  }

  @Override
  public GitObject findByHash(String hash) {
    File f = new File(getPath(hash));
    if (!f.exists()) {
      throw new RuntimeException("File does not exist: " + hash);
    }
    byte[] fileData = null;
    try {
      fileData = Files.readAllBytes(f.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    byte[] rawBytes = ZlibUtils.decompressBlob(fileData);
    return GitObjectFactory.createGitObjectFromRawBytes(rawBytes);
  }

  @Override
  public String save(GitObject gitObject) {
    byte[] rawBytes = gitObject.toByteArray();
    String shaHash = HashUtils.hashInHexFormat(rawBytes);

    // Create directory
    File dir = new File(getDirectory(shaHash));
    dir.mkdirs();

    File f = new File(getPath(shaHash));
    try {
      f.createNewFile();
      Files.write(f.toPath(), ZlibUtils.compressBlob(rawBytes));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return shaHash;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

}
