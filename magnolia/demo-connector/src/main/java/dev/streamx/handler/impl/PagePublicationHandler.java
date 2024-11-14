package dev.streamx.handler.impl;

import dev.streamx.handler.PublicationHandler;
import dev.streamx.handler.StreamXPublisher;
import dev.streamx.util.HtmlUtil;
import dev.streamx.util.StreamXPushSummary;
import info.magnolia.rendering.engine.RenderingEngine;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PagePublicationHandler implements PublicationHandler {

  private static final String PAGE_NODE_TYPE = "mgnl:page";
  private static final String WEBSITE_REPOSITORY = "website";

  private final StreamXPublisher streamxPublisher;

  private final RenderingEngine renderingEngine;

  @Override
  public boolean accept(final Node node, final String repository) {
    try {
      return PAGE_NODE_TYPE.equals(node.getPrimaryNodeType().getName())
          && WEBSITE_REPOSITORY.equals(repository);
    } catch (RepositoryException e) {
      log.error("Cannot get node type of page", e);
      return false;
    }
  }

  @Override
  public List<StreamXPushSummary> handle(final Node node, final String repository, Map<String, Object> properties) {
    try {
      final String uri = node.getPath() + ".html";
      final String htmlContent = HtmlUtil.generatePageHtml(renderingEngine, node);
      return List.of(streamxPublisher.pushPage(uri, htmlContent));
    } catch (final Exception e) {
      return List.of(StreamXPushSummary.unknownPagePublicationError(e.getMessage()));
    }
  }
}
