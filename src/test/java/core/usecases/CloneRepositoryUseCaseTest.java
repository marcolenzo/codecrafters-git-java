package core.usecases;

import infra.persistence.GitObjectFileRepository;
import infra.remote.HttpGitRemoteRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CloneRepositoryUseCaseTest {

  private CloneRepositoryUseCase useCase;
  private GitObjectFileRepository repo;

  @BeforeEach
  void setUp() {
    repo = new GitObjectFileRepository();
    useCase = new CloneRepositoryUseCase(repo, new HttpGitRemoteRepository());
  }


  @Test
  void cloneRepository() throws IOException, URISyntaxException {
    Path basePath = Path.of("/tmp", "hex",
        UUID.randomUUID().toString() + "/");
    String repoUrl = "https://github.com/marcolenzo/oop-design-patterns";
    repo.setBasePath(basePath.toString());

//    String repoUrl = "https://github.com/spring-projects/spring-framework";

    useCase.cloneRepository(repoUrl, basePath);
    System.out.println("files in: " + basePath);
  }

//  @Test
//  void scanLocalRepo() throws IOException, URISyntaxException {
//    Path basePath = Path.of(
//        "/home/marcol/Workspace/github/marcolenzo/design-patterns/.git/objects");
//    Files.walk(basePath).forEach(f -> {
//      Path parent = f.getParent();
//      if (parent.getName(parent.getNameCount() - 1).toString().length() == 2) {
//        System.out.println(
//            parent.getName(parent.getNameCount() - 1).toString() + f.getName(f.getNameCount() - 1)
//                .toString());
//      }
//    });
//  }
//
//  @Test
//  void readHash() throws IOException, URISyntaxException {
//    Path basePath = Path.of(
//        "/home/marcol/Workspace/github/marcolenzo/design-patterns/");
//    repo.setBasePath(basePath.toString());
//    System.out.println(new String(repo.read("884b964db9189cfdf0e7a25ba012ef16f156b3ed")));
//  }
}