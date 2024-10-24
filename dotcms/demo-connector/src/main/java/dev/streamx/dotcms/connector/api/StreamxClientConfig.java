package dev.streamx.dotcms.connector.api;

import lombok.Value;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * Model containing configurable settings for a StreamX ingestion API client.
 */
@Value
public class StreamxClientConfig {
    private static final String STREAMX_HOST_KEY = "streamx.host";
    private static final String STREAMX_AUTH_TOKEN_KEY = "streamx.authToken";
    private static final String DEFAULT_STREAMX_HOST = "http://host.docker.internal:8080";
    private static final String DEFAULT_AUTH_TOKEN = "";

    String host;
    String authToken;

    /**
     * Constructs a StreamX ingestion API client configuration from a {@link Properties} object loaded from any source.
     * Uses default values for configuration entries with missing keys.
     *
     * @param properties non-null properties object loaded from any source
     * @return non-null object populated with resolved entries or with defaults
     */
    public static @Nonnull StreamxClientConfig loadFrom(@Nonnull final Properties properties) {
        final String host = properties.getProperty("streamx.host", "http://host.docker.internal:8080");
        final String authToken = properties.getProperty("streamx.authToken", "");

        return new StreamxClientConfig(host, authToken);
    }
}
