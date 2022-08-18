import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;

class MolokoBlogEngineWireMock {
  public static void main(String[] args) {
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
