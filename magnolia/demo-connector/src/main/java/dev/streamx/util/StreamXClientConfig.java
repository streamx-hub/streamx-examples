package dev.streamx.util;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import org.apache.commons.lang3.StringUtils;
import dev.streamx.StreamXConnector;

public record StreamXClientConfig(String host, String authToken) {

  private static final String DEFAULT_STREAMX_HOST = "http://localhost:8080";
  private static final String DEFAULT_AUTH_TOKEN = "";

  public static @Nonnull StreamXClientConfig loadFrom(
      Provider<StreamXConnector> connectorProvider) {
    StreamXConnector connectorInstance = connectorProvider.get();
    final String host = StringUtils.defaultIfBlank(connectorInstance.getStreamxHost(),
        DEFAULT_STREAMX_HOST);
    final String authToken = StringUtils.defaultIfBlank(connectorInstance.getStreamxAuthToken(),
        DEFAULT_AUTH_TOKEN);

    return new StreamXClientConfig(host, authToken);
  }

}
