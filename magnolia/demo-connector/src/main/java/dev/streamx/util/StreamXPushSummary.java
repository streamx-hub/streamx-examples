package dev.streamx.util;

import javax.annotation.Nonnull;
import dev.streamx.handler.impl.StreamXChannel;

public record StreamXPushSummary(PublicationResult result, String relativePath, StreamXChannel streamxChannel, String errorMessage) {

  /**
   * Static factory method to create summary for a successful push to StreamX.
   *
   * @param relativePath   relative path/URI where the resource was registered on
   * @param streamxChannel which channel the resource was published to
   * @return object containing the summary
   */
  public static @Nonnull StreamXPushSummary success(@Nonnull final String relativePath,
      @Nonnull final StreamXChannel streamxChannel) {
    return new StreamXPushSummary(PublicationResult.SUCCESS, relativePath, streamxChannel, "N/A");
  }

  /**
   * Static factory method to create summary for that scenario when processing failed before pushing
   * anything to StreamX.
   *
   * @param relativePath   relative path/URI where the resource was supposed to register on
   * @param streamxChannel which channel the resource was supposed to published to
   * @param errorMessage   details about the processing error
   * @return object containing the summary
   */
  public static @Nonnull StreamXPushSummary publicationError(@Nonnull final String relativePath,
      @Nonnull final StreamXChannel streamxChannel,
      @Nonnull final String errorMessage) {
    return new StreamXPushSummary(PublicationResult.PUBLICATION_ERROR, relativePath, streamxChannel,
        errorMessage);
  }

  /**
   * Static factory method to create summary for that scenario when processing failed before pushing
   * page to StreamX.
   *
   * @param errorMessage details about the processing error
   * @return object containing the summary
   */
  public static @Nonnull StreamXPushSummary unknownPagePublicationError(
      @Nonnull final String errorMessage) {
    return new StreamXPushSummary(PublicationResult.PUBLICATION_ERROR, "N/A", StreamXChannel.PAGES,
        errorMessage);
  }

  /**
   * Static factory method to create summary for that scenario when processing fails during push to
   * StreamX.
   *
   * @param relativePath   relative path/URI where the resource was supposed to register on
   * @param streamxChannel which channel the resource was supposed to published to
   * @param errorMessage   details about the processing error
   * @return object containing the summary
   */
  public static @Nonnull StreamXPushSummary streamxError(@Nonnull final String relativePath,
      @Nonnull final StreamXChannel streamxChannel,
      @Nonnull final String errorMessage) {
    return new StreamXPushSummary(PublicationResult.STREAMX_ERROR, relativePath, streamxChannel,
        errorMessage);
  }
}
