package dev.streamx.handler.impl;

import dev.streamx.handler.PublicationHandler;
import dev.streamx.handler.StreamXPublisher;
import info.magnolia.jcr.util.NodeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.streamx.util.StreamXPushSummary;

@Slf4j
@RequiredArgsConstructor
public class AssetPublicationHandler implements PublicationHandler {

  private static final String DAM_REPOSITORY = "dam";

  private final StreamXPublisher streamxPublisher;

  @Override
  public boolean accept(Node node, String repository) {
    try {
      return DAM_REPOSITORY.equals(repository) && "/".equals(node.getPath());
    } catch (RepositoryException e) {
      log.error("Cannot get node path", e);
      return false;
    }
  }

  @Override
  public List<StreamXPushSummary> handle(Node node, String repository, Map<String, Object> properties) {
    try {
      List<String> uuids = (List<String>) properties.get("uuids");
      List<StreamXPushSummary> pushSummaries = new ArrayList<>();
      for (String uuid : uuids) {
        Node imageNode = NodeUtil.getNodeByIdentifier(DAM_REPOSITORY, uuid);
        Node binaryImageNode = imageNode.getNode("jcr:content");
        String name = "/" + binaryImageNode.getProperty("fileName").getString();
        Binary imageBinary = binaryImageNode.getProperty("jcr:data").getBinary();
        String path = "/dam/jcr:" + uuid + name;
        pushSummaries.add(streamxPublisher.pushAsset(path, imageBinary.getStream()));
      }
      return pushSummaries;
    } catch (final RepositoryException e) {
      return List.of(StreamXPushSummary.publicationError("N/A", StreamXChannel.ASSETS, e.getMessage()));
    }
  }
}
