package core.usecases;

import core.entities.GitTree;
import core.repository.GitObjectRepository;
import java.io.IOException;

public class LsTreeUseCase {

  private final GitObjectRepository repo;

  public LsTreeUseCase(GitObjectRepository repo) {
    this.repo = repo;
  }

  public GitTree lsTree(String hash) throws IOException {
    return (GitTree) repo.findByHash(hash);
  }

}
