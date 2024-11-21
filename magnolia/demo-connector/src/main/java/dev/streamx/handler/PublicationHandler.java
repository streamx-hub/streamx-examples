package dev.streamx.handler;

import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import dev.streamx.util.StreamXPushSummary;

public interface PublicationHandler {

  boolean accept(Node node, String repository);

  List<StreamXPushSummary> handle(Node node, String repository, Map<String, Object> properties);

}
