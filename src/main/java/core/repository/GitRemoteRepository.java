package core.repository;

import core.entities.GitPack;
import core.entities.vos.GitReferences;
import java.util.Set;

public interface GitRemoteRepository {

  GitReferences getReferences(String repoUrl);

  GitPack getPack(String repoUrl, Set<String> refs);

}
