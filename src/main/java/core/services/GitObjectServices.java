package core.services;

import core.entities.GitBlob;
import core.entities.GitCommit;
import core.entities.GitObject;
import core.entities.GitTree;
import core.enums.GitObjectType;
import core.factories.GitObjectFactory;
import core.repository.GitObjectRepository;

/**
 * Sample service class to interact with the {@link core.entities.GitObject} entity.
 */
public class GitObjectServices {

  private final GitObjectRepository repo;

  public GitObjectServices(GitObjectRepository repo) {
    this.repo = repo;
  }

  public GitObject findByHash(String hash) {
    return repo.findByHash(hash);
  }

  public String createFromBody(GitObjectType type, byte[] body) {
    GitObject gitObject = switch (type) {
      case BLOB -> new GitBlob(body);
      case COMMIT -> new GitCommit(body);
      case TREE -> new GitTree(body);
      default -> throw new RuntimeException("Invalid type");
    };
    return repo.save(gitObject);
  }

  public String createFromRawBytes(byte[] raw) {
    GitObject gitObject = GitObjectFactory.createGitObjectFromRawBytes(raw);
    return repo.save(gitObject);
  }

}
