package infra.remote;

import core.entities.GitPack;
import core.entities.vos.GitReferences;
import core.repository.GitRemoteRepository;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class HttpGitRemoteRepository implements GitRemoteRepository {

  @Override
  public GitReferences getReferences(String repoUrl) {

    String headHash = null;

    try {
      URL url = new URI(repoUrl + "/info/refs?service=git-upload-pack").toURL();

      // Perform request to retrieve info
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/x-git-upload-pack-advertisement");

      int status = connection.getResponseCode();
      if (status != 200) {
        throw new RuntimeException("Failed with HTTP error code : " + status);
      }
      System.out.println(url.toString() + " -> " + status);

      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream()))) {
        String header = reader.readLine();

        // Checking first five bytes as per official docs
        assert header.matches("^[0-9a-f]{4}#.*");
        assert header.endsWith("service=git-upload-pack");

        // Reading all refs
        Set<String> refs = new HashSet<>();
        String line;
        while ((line = reader.readLine()) != null) {
          // Remove leading zeros
          if (line.startsWith("0000")) {
            line = line.substring(4);
          }

          // Skip empty lines
          if (line.isEmpty()) {
            continue;
          }

          // Remove size
          String hash = line.split(" ")[0].substring(4);
          refs.add(hash);
          if (headHash == null) {
            headHash = hash;
          }
        }

        GitReferences gitReferences = new GitReferences(headHash, refs);
        System.out.println(gitReferences);
        return gitReferences;
      }

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public GitPack getPack(String repoUrl, Set<String> refs) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      for (String ref : refs) {
        String line = "want " + ref + "\n";
        int length = line.getBytes().length + 4;
        baos.write(String.format("%04x%s", length, line).getBytes());
      }
      baos.write("00000009done\n".getBytes());

      System.out.println(baos.toString());

      // Pulling each ref
      URL url = new URI(repoUrl + "/git-upload-pack").toURL();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/x-git-upload-pack-request");
      connection.getOutputStream().write(baos.toByteArray());
      baos.close();

      int status = connection.getResponseCode();
      System.out.println(url.toString() + " -> " + status);
      if (status != 200) {
        throw new RuntimeException("Failed with HTTP error code : " + status);
      }

      // Parse pack
      return GitPack.fromInputStream(connection.getInputStream().readAllBytes());
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
