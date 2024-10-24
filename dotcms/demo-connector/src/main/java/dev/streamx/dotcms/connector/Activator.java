package dev.streamx.dotcms.connector;

import com.dotcms.content.elasticsearch.business.event.ContentletPublishEvent;
import com.dotcms.system.event.local.business.LocalSystemEventsAPI;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.loggers.Log4jUtil;
import com.dotmarketing.osgi.GenericBundleActivator;
import com.dotmarketing.util.Logger;
import dev.streamx.clients.ingestion.StreamxClient;
import dev.streamx.clients.ingestion.exceptions.StreamxClientException;
import dev.streamx.dotcms.connector.api.PublicationHandler;
import dev.streamx.dotcms.connector.api.StreamxClientConfig;
import dev.streamx.dotcms.connector.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * OSGi bundle activator to manage the lifecycle of StreamX plugin for dotCMS.
 */
public class Activator extends GenericBundleActivator {

    private LoggerContext pluginLoggerContext;
    private StreamxClient streamxClient;

    /**
     * Initializes standard dotCMS and StreamX plugin services.
     *
     * @param bundleContext context passed in by the OSGi container
     * @throws Exception when any unrecoverable initialization error happens
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        //Initializing log4j...
        LoggerContext dotcmsLoggerContext = Log4jUtil.getLoggerContext();
        //Initialing the log4j context of this plugin based on the dotCMS logger context
        pluginLoggerContext = (LoggerContext) LogManager
                .getContext(this.getClass().getClassLoader(),
                        false,
                        dotcmsLoggerContext,
                        dotcmsLoggerContext.getConfigLocation());

        Logger.info(this.getClass(), "Starting StreamX plugin for dotCMS...");

        initializeServices(bundleContext);
        initializePluginServices();

        Logger.info(this.getClass(), "Started StreamX plugin for dotCMS.");
    }


    /**
     * Shuts down StreamX plugin services and unregisters listeners.
     *
     * @param bundleContext context passed in by the OSGi container
     * @throws Exception when any error happens during shutdown
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Logger.info(this.getClass(), "Stopping StreamX plugin for dotCMS...");

        unregisterServices(bundleContext);
        shutdownPluginServices();
        Log4jUtil.shutdown(pluginLoggerContext);

        Logger.info(this.getClass(), "Stopped StreamX plugin for dotCMS.");
    }

    private void initializePluginServices() throws StreamxClientException {
        //without full support of OSGi DS, construct plugin services manually
        final StreamxClientConfig streamxClientConfig = loadStreamxClientConfig();
        streamxClient = StreamxClientFactory.newStreamxClient(streamxClientConfig);
        final PublicationListener publicationListener = createPublicationListener(streamxClient);

        //dotCMS subscription is registered by "<class_name>#<handler_method>" and the type of the event subscribed
        final LocalSystemEventsAPI localSystemEventsAPI = APILocator.getLocalSystemEventsAPI();
        localSystemEventsAPI.subscribe(publicationListener);
    }

    private PublicationListener createPublicationListener(final StreamxClient streamxClient) {
        final StreamxPublisherImpl streamxPublisher = new StreamxPublisherImpl(streamxClient);

        final List<PublicationHandler> publicationHandlers = List.of(
                new PagePublicationHandler(streamxPublisher),
                new UrlMappedPagePublicationHandler(streamxPublisher),
                new DotAssetPublicationHandler(streamxPublisher),
                new CssPublicationHandler(streamxPublisher)
        );

        return new PublicationListener(publicationHandlers);
    }

    private void shutdownPluginServices() throws StreamxClientException {
        //unregister our subscription for publication events
        APILocator.getLocalSystemEventsAPI().unsubscribe(ContentletPublishEvent.class, PublicationListener.class.getName() + "#onPublish");

        if (streamxClient != null) {
            streamxClient.close();
        }
    }

    private StreamxClientConfig loadStreamxClientConfig() {

        try (final InputStream in = this.getClass().getResourceAsStream("/plugin.properties")) {
            final Properties properties = new Properties();
            properties.load(in);

            return StreamxClientConfig.loadFrom(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
