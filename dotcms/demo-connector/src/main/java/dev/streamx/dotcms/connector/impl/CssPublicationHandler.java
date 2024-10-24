package dev.streamx.dotcms.connector.impl;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import dev.streamx.dotcms.connector.api.PublicationHandler;
import dev.streamx.dotcms.connector.api.StreamxChannel;
import dev.streamx.dotcms.connector.api.StreamxPublisher;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

/**
 * Handles dotCMS CSS files and forwards them to StreamX.
 */
public class CssPublicationHandler implements PublicationHandler {

    private final StreamxPublisher streamxPublisher;

    public CssPublicationHandler(final StreamxPublisher streamxPublisher) {
        this.streamxPublisher = streamxPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(@Nonnull final Contentlet contentlet) {
        return contentlet.isFileAsset() && contentlet.getName().endsWith(".css");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public List<StreamxPushSummary> handle(@Nonnull final Contentlet contentlet) {
        try {
            final String uri = APILocator.getIdentifierAPI().find(contentlet.getIdentifier()).getURI();
            final File file = APILocator.getFileAssetAPI().fromContentlet(contentlet).getFileAsset();

            return List.of(streamxPublisher.pushWebResource(uri, file));
        } catch (final Exception exc) {
            return List.of(StreamxPushSummary.publicationError("N/A", StreamxChannel.WEB_RESOURCES, exc.getMessage()));
        }
    }
}
