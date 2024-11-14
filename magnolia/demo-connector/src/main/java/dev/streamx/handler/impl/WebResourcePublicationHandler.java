package dev.streamx.handler.impl;

import dev.streamx.handler.PublicationHandler;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.streamx.handler.StreamXPublisher;
import dev.streamx.util.StreamXPushSummary;

@Slf4j
@RequiredArgsConstructor
public class WebResourcePublicationHandler implements PublicationHandler {

  private static final String RESOURCES_REPOSITORY = "resources";
  private static final String CONTENT_NODE_TYPE = "mgnl:content";

  private final StreamXPublisher streamxPublisher;

  @Override
  public boolean accept(Node node, String repository) {
    try {
      return CONTENT_NODE_TYPE.equals(node.getPrimaryNodeType().getName())
          && RESOURCES_REPOSITORY.equals(repository);
    } catch (RepositoryException e) {
      log.error("Cannot get node type of web resource", e);
      return false;
    }
  }

  @Override
  public List<StreamXPushSummary> handle(Node node, String repository, Map<String, Object> properties) {
    try {
      String path = ".resources" + node.getPath();
      String resourceText = node.getProperty("text").getString();
      InputStream resourceStream = new ByteArrayInputStream(resourceText.getBytes(StandardCharsets.UTF_8));
      return List.of(streamxPublisher.pushWebResource(path, resourceStream));
    } catch (final RepositoryException e) {
      return List.of(StreamXPushSummary.publicationError("N/A", StreamXChannel.WEB_RESOURCES, e.getMessage()));
    }
  }
}
