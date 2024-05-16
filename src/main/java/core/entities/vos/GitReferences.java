package core.entities.vos;

import java.util.Set;

public record GitReferences(String headHash, Set<String> refs) {

}
