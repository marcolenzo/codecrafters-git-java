package core.repository;

import core.entities.GitObject;

/**
 * Retrieves and stores {@link GitObject} on a data store.
 */
public interface GitObjectRepository {

  public static final String DEFAULT_PATH = ".git/objects";

  /**
   * Retrieve object by hash
   *
   * @param shaHash
   * @return The object associated with the hash
   */
  GitObject findByHash(String shaHash);

  /**
   * Save object
   *
   * @param gitObject
   * @return The hash of the saved object
   */
  String save(GitObject gitObject);

}
