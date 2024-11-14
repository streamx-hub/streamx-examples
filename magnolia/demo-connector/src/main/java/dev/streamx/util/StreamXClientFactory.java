package dev.streamx.util;

import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamXClientFactory {

  @Nonnull
  public static StreamxClient newStreamxClient(@Nonnull final StreamXClientConfig streamxClientConfig) throws StreamxClientException {
    final StreamxClient streamxClient = StreamxClient.builder(streamxClientConfig.host())
        .setAuthToken(!StringUtils.isBlank(streamxClientConfig.authToken()) ? streamxClientConfig.authToken() : null)
        .setApacheHttpClient(newHttpClient())
        .build();

    log.info("Successfully created new StreamX client");

    return streamxClient;
  }

  private static CloseableHttpClient newHttpClient() {
    final PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();

    httpClientConnectionManager.setMaxTotal(50);
    httpClientConnectionManager.setDefaultMaxPerRoute(20);

    return HttpClientBuilder.create()
        .setConnectionManager(httpClientConnectionManager)
        .setKeepAliveStrategy((response, context) -> 60000)
        .evictIdleConnections(30000, TimeUnit.MILLISECONDS)
        .evictExpiredConnections()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setConnectTimeout(10000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(10000)
            .build())
        .build();
  }

}
