package dev.streamx.dotcms.connector.impl;

import com.dotmarketing.util.Logger;
import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import dev.streamx.clients.ingestion.publisher.Publisher;
import dev.streamx.dotcms.connector.api.StreamxChannel;
import dev.streamx.dotcms.connector.api.StreamxPublisher;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;
import lombok.Value;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Supports pushing HTML pages, assets and web resources to StreamX.
 */
public class StreamxPublisherImpl implements StreamxPublisher {

    private final Publisher<Data> pagePublisher;
    private final Publisher<Data> assetPublisher;
    private final Publisher<Data> webResourcePublisher;

    public StreamxPublisherImpl(final StreamxClient streamxClient) {
        pagePublisher = streamxClient.newPublisher(StreamxChannel.PAGES.getName(), Data.class);
        assetPublisher = streamxClient.newPublisher(StreamxChannel.ASSETS.getName(), Data.class);
        webResourcePublisher = streamxClient.newPublisher(StreamxChannel.WEB_RESOURCES.getName(), Data.class);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public StreamxPushSummary pushPage(@Nonnull final String relativePath, @Nonnull final String html) {
        try (final InputStream inputStream = IOUtils.toInputStream(html, Charset.defaultCharset())) {
            final Data data = new Data(ByteBuffer.wrap(inputStream.readAllBytes()));

            return push(relativePath, data, pagePublisher, StreamxChannel.PAGES);
        } catch (final Exception exc) {
            Logger.error(this.getClass(), "Unknown error during pushing page to StreamX on relative path: " + relativePath, exc);
            return StreamxPushSummary.publicationError(relativePath, StreamxChannel.PAGES, exc.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public StreamxPushSummary pushAsset(@Nonnull final String relativePath, @Nonnull final File file) {
        return push(relativePath, file, assetPublisher, StreamxChannel.ASSETS);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public StreamxPushSummary pushWebResource(@Nonnull final String relativePath, @Nonnull final File file) {
        return push(relativePath, file, webResourcePublisher, StreamxChannel.WEB_RESOURCES);
    }

    private StreamxPushSummary push(final String relativePath, final File file,
                                    final Publisher<Data> publisher, final StreamxChannel streamxChannel) {
        try {
            final Data data = new Data(ByteBuffer.wrap(Files.readAllBytes(file.toPath())));

            return push(relativePath, data, publisher, streamxChannel);
        } catch (final Exception exc) {
            Logger.error(this.getClass(), "Unknown error during pushing resource to StreamX on relative path: " + relativePath, exc);
            return StreamxPushSummary.publicationError(relativePath, streamxChannel, exc.getMessage());
        }
    }

    private StreamxPushSummary push(final String relativePath, final Data data,
                                    final Publisher<Data> publisher, final StreamxChannel streamxChannel) {
        try {
            publisher.publish(relativePath, data);

            return StreamxPushSummary.success(relativePath, streamxChannel);
        } catch (final StreamxClientException exc) {
            Logger.error(this.getClass(), "Cannot push content to StreamX", exc);
            return StreamxPushSummary.streamxError(relativePath, streamxChannel, exc.getMessage());
        }
    }

    @Value
    private static class Data {
        ByteBuffer content;
    }
}
