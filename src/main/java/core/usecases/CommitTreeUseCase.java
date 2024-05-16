package core.usecases;

import core.entities.GitCommit;
import core.repository.GitObjectRepository;
import java.util.List;

public class CommitTreeUseCase {

  private final GitObjectRepository repo;

  public CommitTreeUseCase(GitObjectRepository repo) {
    this.repo = repo;
  }

  public String commitTree(String treeHash, String parentCommitHash, String message) {
    String hardcodedAuthor = "Marco <marco@marcolenzo.eu> 946684800 +0000";
    byte[] body = GitCommit.serialize(treeHash,
        List.of(parentCommitHash),
        List.of(hardcodedAuthor), List.of(), message);

    GitCommit gitCommit = new GitCommit(body);

    return repo.save(gitCommit);
  }

}
