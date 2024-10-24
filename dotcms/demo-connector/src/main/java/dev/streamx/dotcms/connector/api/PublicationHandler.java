package dev.streamx.dotcms.connector.api;

import com.dotmarketing.portlets.contentlet.model.Contentlet;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Contract for handlers capable of transforming and pushing dotCMS content to StreamX during standard dotCMS publication.
 */
public interface PublicationHandler {

    /**
     * Checks if this handler supports the given dotCMS content
     *
     * @param contentlet a published dotCMS contentlet
     * @return true if this handler is interested in the given content, false otherwise
     */
    boolean accept(@Nonnull Contentlet contentlet);

    /**
     * Transforms and pushes the given dotCMS content to StreamX.
     * API consumers must call the {@link PublicationHandler#accept(Contentlet)}
     * beforehand to make sure that this handler is capable to deal with the given content.
     *
     * @param contentlet a published dotCMS contentlet
     * @return list of objects containing detailed information about the processed content
     */
    @Nonnull
    List<StreamxPushSummary> handle(@Nonnull Contentlet contentlet);
}
