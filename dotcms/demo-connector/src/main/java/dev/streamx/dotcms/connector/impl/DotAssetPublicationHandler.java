package dev.streamx.dotcms.connector.impl;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import dev.streamx.dotcms.connector.api.PublicationHandler;
import dev.streamx.dotcms.connector.api.StreamxChannel;
import dev.streamx.dotcms.connector.api.StreamxPublisher;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Handles dotCMS dotAssets (images) and forwards them to StreamX.
 */
public class DotAssetPublicationHandler implements PublicationHandler {

    private final StreamxPublisher streamxPublisher;

    public DotAssetPublicationHandler(StreamxPublisher streamxPublisher) {
        this.streamxPublisher = streamxPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(@Nonnull final Contentlet contentlet) {
        return contentlet.isDotAsset();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public List<StreamxPushSummary> handle(@Nonnull final Contentlet contentlet) {
        try {
            final String uri = "/dA/" + APILocator.getShortyAPI().shortify(contentlet.getIdentifier()) + "/assets/" + contentlet.getName();
            final File file = contentlet.getBinary("asset");

            return List.of(streamxPublisher.pushAsset(uri, file));
        } catch (final IOException exc) {
            return List.of(StreamxPushSummary.publicationError("N/A", StreamxChannel.ASSETS, exc.getMessage()));
        }
    }
}
