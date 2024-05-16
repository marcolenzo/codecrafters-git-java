package core.usecases;

import core.entities.GitObject;
import core.repository.GitObjectRepository;

public class CatFileUseCase {

  private final GitObjectRepository repo;

  public CatFileUseCase(GitObjectRepository repo) {
    this.repo = repo;
  }

  public GitObject getContent(String hash) {
    return repo.findByHash(hash);
  }

}
