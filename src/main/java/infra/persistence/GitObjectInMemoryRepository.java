package infra.persistence;

import core.entities.GitObject;
import core.repository.GitObjectRepository;
import core.utils.HashUtils;
import java.util.HashMap;

/**
 * In memory implementation of {@link GitObjectRepository} useful for testing and development.
 */
public class GitObjectInMemoryRepository implements GitObjectRepository {

  private final HashMap<String, GitObject> objectsMap = new HashMap<>();

  @Override
  public GitObject findByHash(String hash) {
    return objectsMap.get(hash);
  }

  @Override
  public String save(GitObject gitObject) {
    String hash = HashUtils.hashInHexFormat(gitObject.toByteArray());
    objectsMap.put(hash, gitObject);
    return hash;
  }
  
}
