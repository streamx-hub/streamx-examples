package dev.streamx.dotcms.connector.impl;

import com.dotcms.content.elasticsearch.business.event.ContentletPublishEvent;
import com.dotcms.system.event.local.model.Subscriber;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;
import dev.streamx.dotcms.connector.api.PublicationHandler;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Listens to publication-related events emitted by dotCMS.
 */
public class PublicationListener {

    private final List<PublicationHandler> handlers;

    public PublicationListener(final List<PublicationHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * Subscribes to and handle publish events.
     * The published contentlet is run through a series of handlers to filter and push relevant content into StreamX.
     *
     * @param contentletPublishEvent the publication event emitted by dotCMS
     */
    @Subscriber
    public void onPublish(final ContentletPublishEvent<Contentlet> contentletPublishEvent) {
        final Contentlet contentlet = contentletPublishEvent.getContentlet();

        if (contentlet != null) {
            final List<StreamxPushSummary> streamxPushSummaries = handlers.stream()
                    .filter(handler -> handler.accept(contentlet))
                    .map(handler -> handler.handle(contentlet))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            if (!streamxPushSummaries.isEmpty()) {

                Logger.info(this.getClass(), "For contentlet with ID '" + contentlet.getIdentifier()
                        + "', the following actions were taken:");
                for (final StreamxPushSummary streamxPushSummary : streamxPushSummaries) {
                    Logger.info(this.getClass(), "\t" + streamxPushSummary);
                }
            }
        }
    }
}
