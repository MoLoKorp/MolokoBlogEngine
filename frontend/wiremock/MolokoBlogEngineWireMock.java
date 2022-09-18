import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

class MolokoBlogEngineWireMock {
  private static final String FILES = "frontend/wiremock/mocks/__files";

  public static void main(String[] args) throws IOException {
    copyFilesToWiremockFolder("/views");
    copyFilesToWiremockFolder("/css");

    WireMockServer wireMockServer =
        new WireMockServer(
            options()
                .port(8089)
                .usingFilesUnderClasspath("frontend/wiremock/mocks")
                .stubCorsEnabled(true));
    try {
      wireMockServer.start();
      while (true) {}
    } finally {
      wireMockServer.stop();
    }
  }

  private static void copyFilesToWiremockFolder(String from) throws IOException {
    if (Files.notExists(Path.of(FILES + from))) {
      Files.createDirectory(Path.of(FILES + from));
    }
    Files.newDirectoryStream(Path.of("frontend" + from))
        .forEach(
            file -> {
              try {
                Files.copy(
                    file,
                    Path.of(FILES + from + "/" + file.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
  }
}
