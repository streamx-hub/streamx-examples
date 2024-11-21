package dev.streamx.handler.impl;

import dev.streamx.blueprints.data.Asset;
import dev.streamx.blueprints.data.Page;
import dev.streamx.blueprints.data.WebResource;
import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import dev.streamx.clients.ingestion.publisher.Publisher;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.streamx.handler.StreamXPublisher;
import dev.streamx.util.StreamXPushSummary;

@Slf4j
public class StreamXPublisherImpl implements StreamXPublisher {

  private final Publisher<Page> pagePublisher;
  private final Publisher<Asset> assetPublisher;
  private final Publisher<WebResource> webResourcePublisher;

  public StreamXPublisherImpl(final StreamxClient streamxClient) throws StreamxClientException {
    pagePublisher = streamxClient.newPublisher(StreamXChannel.PAGES.getName(), Page.class);
    assetPublisher = streamxClient.newPublisher(StreamXChannel.ASSETS.getName(), Asset.class);
    webResourcePublisher = streamxClient.newPublisher(StreamXChannel.WEB_RESOURCES.getName(), WebResource.class);
  }

  @Override
  public StreamXPushSummary pushPage(@Nonnull final String relativePath, @Nonnull final String html) {
    final Page page = new Page(ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8)));
    try {
      pagePublisher.publish(relativePath, page);
      return StreamXPushSummary.success(relativePath, StreamXChannel.PAGES);
    } catch (final StreamxClientException e) {
      log.error("Cannot push page to StreamX", e);
      return StreamXPushSummary.streamxError(relativePath, StreamXChannel.PAGES, e.getMessage());
    }
  }

  @Override
  public StreamXPushSummary pushAsset(@Nonnull final String relativePath, @Nonnull final InputStream inputStream) {
    try {
      final Asset asset = new Asset(ByteBuffer.wrap(inputStream.readAllBytes()));
      assetPublisher.publish(relativePath, asset);
      return StreamXPushSummary.success(relativePath, StreamXChannel.ASSETS);
    } catch (Exception e) {
      log.error("Cannot push asset to StreamX", e);
      return StreamXPushSummary.streamxError(relativePath, StreamXChannel.ASSETS, e.getMessage());
    }
  }

  @Override
  public StreamXPushSummary pushWebResource(@Nonnull final String relativePath, @Nonnull final InputStream inputStream) {
    try {
      final WebResource webResource = new WebResource(ByteBuffer.wrap(inputStream.readAllBytes()));
      webResourcePublisher.publish(relativePath, webResource);
      return StreamXPushSummary.success(relativePath, StreamXChannel.WEB_RESOURCES);
    } catch (Exception e) {
      log.error("Cannot push web resource to StreamX", e);
      return StreamXPushSummary.streamxError(relativePath, StreamXChannel.WEB_RESOURCES, e.getMessage());
    }
  }
}
