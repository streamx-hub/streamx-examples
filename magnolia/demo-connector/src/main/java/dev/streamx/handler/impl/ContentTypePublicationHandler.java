package dev.streamx.handler.impl;

import dev.streamx.handler.PublicationHandler;
import dev.streamx.handler.StreamXPublisher;
import dev.streamx.util.HtmlUtil;
import dev.streamx.util.StreamXPushSummary;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.rendering.engine.RenderingEngine;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ContentTypePublicationHandler implements PublicationHandler {

  private static final String PAGE_NODE_TYPE = "mgnl:content";
  private static final String WEBSITE_REPOSITORY = "website";

  private final StreamXPublisher streamXPublisher;

  private final RenderingEngine renderingEngine;

  @Override
  public boolean accept(Node node, String repository) {
    try {
      return PAGE_NODE_TYPE.equals(node.getPrimaryNodeType().getName())
          && node.hasProperty("detailPagePath")
          && node.hasProperty("renderedPageParent");
    } catch (RepositoryException e) {
      log.error("Cannot get node type of page", e);
      return false;
    }
  }

  @Override
  public List<StreamXPushSummary> handle(Node node, String repository,
      Map<String, Object> properties) {
    try {
      String typeInstanceName = node.getName();
      Node parentNode = NodeUtil.getNodeByIdentifier(WEBSITE_REPOSITORY, node.getProperty("renderedPageParent").getString());
      PagePublicationHandler pagePublicationHandler = new PagePublicationHandler(streamXPublisher, renderingEngine);
      if (pagePublicationHandler.accept(parentNode, WEBSITE_REPOSITORY)) {
        pagePublicationHandler.handle(parentNode, WEBSITE_REPOSITORY, properties);
      }
      String parentPath = parentNode.getPath();
      Node detailPageNode = NodeUtil.getNodeByIdentifier(WEBSITE_REPOSITORY, node.getProperty("detailPagePath").getString());
      String uri = parentPath + "/" + typeInstanceName + ".html";
      MgnlContext.setAttribute("item", typeInstanceName);
      final String htmlContent = HtmlUtil.generatePageHtml(renderingEngine, detailPageNode);
      return List.of(streamXPublisher.pushPage(uri, htmlContent));
    } catch (final Exception e) {
      return List.of(StreamXPushSummary.unknownPagePublicationError(e.getMessage()));
    }
  }
}
