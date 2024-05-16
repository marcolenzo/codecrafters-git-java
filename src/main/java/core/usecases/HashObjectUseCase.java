package core.usecases;

import core.entities.GitBlob;
import core.repository.GitObjectRepository;

/**
 * Creates a {@link GitBlob} from the provided raw bytes and saves it to the
 * {@link GitObjectRepository}.
 */
public class HashObjectUseCase {

  private final GitObjectRepository repo;

  public HashObjectUseCase(GitObjectRepository repo) {
    this.repo = repo;
  }

  public String hashObject(byte[] blob) {
    GitBlob gitBlob = new GitBlob(blob);
    return repo.save(gitBlob);
  }

}
