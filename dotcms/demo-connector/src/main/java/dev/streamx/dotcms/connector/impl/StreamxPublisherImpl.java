package dev.streamx.dotcms.connector.impl;

import com.dotmarketing.util.Logger;
import dev.streamx.blueprints.data.Asset;
import dev.streamx.blueprints.data.Page;
import dev.streamx.blueprints.data.WebResource;
import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import dev.streamx.clients.ingestion.publisher.Publisher;
import dev.streamx.dotcms.connector.api.StreamxChannel;
import dev.streamx.dotcms.connector.api.StreamxPublisher;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;
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

    private final Publisher<Page> pagePublisher;
    private final Publisher<Asset> assetPublisher;
    private final Publisher<WebResource> webResourcePublisher;

    public StreamxPublisherImpl(final StreamxClient streamxClient) throws StreamxClientException {
        pagePublisher = streamxClient.newPublisher(StreamxChannel.PAGES.getName(), Page.class);
        assetPublisher = streamxClient.newPublisher(StreamxChannel.ASSETS.getName(), Asset.class);
        webResourcePublisher = streamxClient.newPublisher(StreamxChannel.WEB_RESOURCES.getName(), WebResource.class);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public StreamxPushSummary pushPage(@Nonnull final String relativePath, @Nonnull final String html) {
        try (final InputStream inputStream = IOUtils.toInputStream(html, Charset.defaultCharset())) {
            final Page page = new Page(ByteBuffer.wrap(inputStream.readAllBytes()));
            pagePublisher.publish(relativePath, page);
            return StreamxPushSummary.success(relativePath, StreamxChannel.PAGES);
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
        try {
            final Asset asset = new Asset(ByteBuffer.wrap(Files.readAllBytes(file.toPath())));
            assetPublisher.publish(relativePath, asset);
            return StreamxPushSummary.success(relativePath, StreamxChannel.ASSETS);
        } catch (final Exception exc) {
            Logger.error(this.getClass(), "Unknown error during pushing asset to StreamX on relative path: " + relativePath, exc);
            return StreamxPushSummary.publicationError(relativePath, StreamxChannel.ASSETS, exc.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public StreamxPushSummary pushWebResource(@Nonnull final String relativePath, @Nonnull final File file) {
        try {
            final WebResource webResource = new WebResource(ByteBuffer.wrap(Files.readAllBytes(file.toPath())));
            webResourcePublisher.publish(relativePath, webResource);
            return StreamxPushSummary.success(relativePath, StreamxChannel.WEB_RESOURCES);
        } catch (final Exception exc) {
            Logger.error(this.getClass(), "Unknown error during pushing web resource to StreamX on relative path: " + relativePath, exc);
            return StreamxPushSummary.publicationError(relativePath, StreamxChannel.WEB_RESOURCES, exc.getMessage());
        }
    }
}
