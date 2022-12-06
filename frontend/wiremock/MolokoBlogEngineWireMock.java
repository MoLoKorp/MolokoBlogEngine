import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class MolokoBlogEngineWireMock {
  private static final String FILES = "frontend/wiremock/mocks/__files";

  public static void main(String[] args) throws IOException {
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
}
