package dev.streamx.dotcms.connector.api;

import lombok.Value;

import javax.annotation.Nonnull;

/**
 * Model to provide information about the outcome of a single resource publication to StreamX.
 */
@Value
public class StreamxPushSummary {

    PublicationResult result;
    String relativePath;
    StreamxChannel streamxChannel;
    Long eventId;
    String errorMessage;

    /**
     * Static factory method to create summary for a successful push to StreamX.
     *
     * @param relativePath   relative path/URI where the resource was registered on
     * @param streamxChannel which channel the resource was published to
     * @param eventId        the event ID returned by the StreamX ingestion API
     * @return object containing the summary
     */
    public static @Nonnull StreamxPushSummary success(@Nonnull final String relativePath,
                                                      @Nonnull final StreamxChannel streamxChannel,
                                                      @Nonnull final Long eventId) {
        return new StreamxPushSummary(PublicationResult.SUCCESS, relativePath, streamxChannel, eventId, "N/A");
    }

    /**
     * Static factory method to create summary for that scenario when processing failed before pushing anything to StreamX.
     *
     * @param relativePath   relative path/URI where the resource was supposed to register on
     * @param streamxChannel which channel the resource was supposed to published to
     * @param errorMessage   details about the processing error
     * @return object containing the summary
     */
    public static @Nonnull StreamxPushSummary publicationError(@Nonnull final String relativePath,
                                                               @Nonnull final StreamxChannel streamxChannel,
                                                               @Nonnull final String errorMessage) {
        return new StreamxPushSummary(PublicationResult.PUBLICATION_ERROR, relativePath, streamxChannel, -1L, errorMessage);
    }

    /**
     * Static factory method to create summary for that scenario when processing fails during push to StreamX.
     *
     * @param relativePath   relative path/URI where the resource was supposed to register on
     * @param streamxChannel which channel the resource was supposed to published to
     * @param errorMessage   details about the processing error
     * @return object containing the summary
     */
    public static @Nonnull StreamxPushSummary streamxError(@Nonnull final String relativePath,
                                                           @Nonnull final StreamxChannel streamxChannel,
                                                           @Nonnull final String errorMessage) {
        return new StreamxPushSummary(PublicationResult.STREAMX_ERROR, relativePath, streamxChannel, -1L, errorMessage);
    }
}
