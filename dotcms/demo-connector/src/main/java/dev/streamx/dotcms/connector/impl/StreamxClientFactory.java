package dev.streamx.dotcms.connector.impl;

import com.dotmarketing.util.Logger;
import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import dev.streamx.dotcms.connector.api.StreamxClientConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * Utility class to create StreamX ingestion API clients.
 */
public final class StreamxClientFactory {

    private StreamxClientFactory() {
    }

    /**
     * Creates a new StreamX client.
     *
     * @param streamxClientConfig object containing the configuration to be used for the StreamX client
     * @return a new StreamX client
     * @throws StreamxClientException when StreamX client cannot be created
     */
    @Nonnull
    public static StreamxClient newStreamxClient(@Nonnull final StreamxClientConfig streamxClientConfig) throws StreamxClientException {
        final StreamxClient streamxClient = StreamxClient.builder(streamxClientConfig.getHost())
                .setAuthToken(!StringUtils.isBlank(streamxClientConfig.getAuthToken()) ? streamxClientConfig.getAuthToken() : null)
                .setApacheHttpClient(newHttpClient())
                .build();

        Logger.info(StreamxClientFactory.class, "Created new StreamX client with configuration: " + streamxClientConfig);

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
