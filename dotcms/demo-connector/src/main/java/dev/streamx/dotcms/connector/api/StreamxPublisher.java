package dev.streamx.dotcms.connector.api;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Contract for pushing dotCMS content to StreamX.
 */
public interface StreamxPublisher {

    /**
     * Pushes an HTML page to StreamX.
     *
     * @param relativePath the relative path to the page, e.g. '/blog.html'
     * @param html         the generated HTML content of the page
     * @return an object summarizing the result of the StreamX push
     */
    @Nonnull
    StreamxPushSummary pushPage(@Nonnull String relativePath, @Nonnull String html);

    /**
     * Pushes any file-based asset to StreamX.
     *
     * @param relativePath the relative path to the asset, e.g. '/dA/35bccd58d1/assets/demo-image-1.webp'
     * @param file         the content of the asset to push
     * @return an object summarizing the result of the StreamX push
     */
    @Nonnull
    StreamxPushSummary pushAsset(@Nonnull String relativePath, @Nonnull File file);

    /**
     * Pushes file-based web resources (e.g. CSS or JS files) to StreamX.
     *
     * @param relativePath the relative path to the resource, e.g. '/application/css/output.css'
     * @param file         the content of the resource to push
     * @return an object summarizing the result of the StreamX push
     */
    @Nonnull
    StreamxPushSummary pushWebResource(@Nonnull String relativePath, @Nonnull File file);
}
