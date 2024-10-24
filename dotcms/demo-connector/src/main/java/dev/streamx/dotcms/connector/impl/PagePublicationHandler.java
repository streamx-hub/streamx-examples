package dev.streamx.dotcms.connector.impl;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.htmlpageasset.business.HTMLPageAssetAPI;
import com.dotmarketing.portlets.htmlpageasset.model.IHTMLPage;
import com.liferay.portal.model.User;
import dev.streamx.dotcms.connector.api.PublicationHandler;
import dev.streamx.dotcms.connector.api.StreamxChannel;
import dev.streamx.dotcms.connector.api.StreamxPublisher;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

import static com.dotmarketing.util.Constants.USER_AGENT_DOTCMS_BROWSER;

/**
 * Handles dotCMS Pages and forwards them to StreamX.
 */
public class PagePublicationHandler implements PublicationHandler {

    private final StreamxPublisher streamxPublisher;

    public PagePublicationHandler(final StreamxPublisher streamxPublisher) {
        this.streamxPublisher = streamxPublisher;
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(@Nonnull final Contentlet contentlet) {
        return contentlet.getStructure().isHTMLPageAsset() && isNotDetailPageTemplate(contentlet);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    public List<StreamxPushSummary> handle(@Nonnull final Contentlet contentlet) {
        try {
            final String uri = APILocator.getIdentifierAPI().find(contentlet.getIdentifier()).getURI();
            final String htmlContent = generatePageHtml(contentlet);

            return List.of(streamxPublisher.pushPage(uri, htmlContent));
        } catch (final Exception exc) {
            return List.of(StreamxPushSummary.publicationError("N/A", StreamxChannel.PAGES, exc.getMessage()));
        }
    }

    private boolean isNotDetailPageTemplate(final Contentlet contentlet) {
        try {
            final String uri = APILocator.getIdentifierAPI().find(contentlet.getIdentifier()).getURI();
            boolean isVelocityTemplate = StringUtils.startsWith(uri, "/application/vtl/");

            return !isVelocityTemplate;
        } catch (DotDataException e) {
            return true;
        }
    }

    private String generatePageHtml(final Contentlet contentlet) throws DotDataException, DotSecurityException {
        final HTMLPageAssetAPI htmlPageAssetApi = APILocator.getHTMLPageAssetAPI();
        final User systemUser = APILocator.getUserAPI().getSystemUser();
        final long defaultLanguageId = APILocator.getLanguageAPI().getDefaultLanguage().getId();

        final IHTMLPage htmlPage = htmlPageAssetApi
                .findByIdLanguageFallback(contentlet.getIdentifier(), defaultLanguageId, true, systemUser, false);
        final Host site = APILocator.getHostAPI()
                .find(htmlPage.getHost(), systemUser, true);

        return htmlPageAssetApi.getHTML(htmlPage.getURI(), site, true, htmlPage.getInode(),
                systemUser, defaultLanguageId, USER_AGENT_DOTCMS_BROWSER);
    }
}
