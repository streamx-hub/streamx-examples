package dev.streamx.command;

import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import dev.streamx.handler.PublicationHandler;
import info.magnolia.commands.impl.BaseRepositoryCommand;
import info.magnolia.context.Context;
import info.magnolia.rendering.engine.RenderingEngine;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.jcr.Node;
import lombok.extern.slf4j.Slf4j;
import dev.streamx.StreamXConnector;
import dev.streamx.handler.impl.AssetPublicationHandler;
import dev.streamx.handler.impl.ContentTypePublicationHandler;
import dev.streamx.handler.impl.PagePublicationHandler;
import dev.streamx.handler.impl.StreamXPublisherImpl;
import dev.streamx.handler.impl.WebResourcePublicationHandler;
import dev.streamx.util.StreamXClientConfig;
import dev.streamx.util.StreamXClientFactory;
import dev.streamx.util.StreamXPushSummary;

@Slf4j
public class StreamXPublicationCommand extends BaseRepositoryCommand {

  private final RenderingEngine renderingEngine;

  private final StreamxClient streamxClient;

  private final Provider<StreamXConnector> connectorProvider;

  private final List<PublicationHandler> handlers;

  @Inject
  public StreamXPublicationCommand (RenderingEngine renderingEngine, Provider<StreamXConnector> streamXConnectorProvider) throws StreamxClientException {
    this.renderingEngine = renderingEngine;
    this.connectorProvider = streamXConnectorProvider;
    final StreamXClientConfig streamxClientConfig = StreamXClientConfig.loadFrom(connectorProvider);
    streamxClient = StreamXClientFactory.newStreamxClient(streamxClientConfig);
    StreamXPublisherImpl streamXPublisher = new StreamXPublisherImpl(streamxClient);
    this.handlers = List.of(
        new PagePublicationHandler(streamXPublisher, this.renderingEngine),
        new AssetPublicationHandler(streamXPublisher),
        new WebResourcePublicationHandler(streamXPublisher),
        new ContentTypePublicationHandler(streamXPublisher, this.renderingEngine)
    );
  }

  @Override
  public boolean execute(Context context) throws Exception {
    Node toPublish = this.getJCRNode(context);
    String repository = context.getAttributes().get("repository").toString();

    List<StreamXPushSummary> pushSummaries = handlers.stream()
        .filter(handler -> handler.accept(toPublish, repository))
        .map(handler -> handler.handle(toPublish, repository, context.getAttributes()))
        .flatMap(Collection::stream)
        .toList();

    if (!pushSummaries.isEmpty()) {
      log.info("For node with ID '{}', the following actions were taken:", toPublish.getIdentifier());
      for (final StreamXPushSummary pushSummary : pushSummaries) {
        log.info("Summary: {}", pushSummary);
      }
    }

    return true;
  }
}
