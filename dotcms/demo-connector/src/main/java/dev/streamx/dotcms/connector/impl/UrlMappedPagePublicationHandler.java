package dev.streamx.dotcms.connector.impl;

import com.dotcms.contenttype.model.type.UrlMapable;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.htmlpageasset.business.HTMLPageAssetAPI;
import com.dotmarketing.portlets.htmlpageasset.model.IHTMLPage;
import com.dotmarketing.portlets.languagesmanager.business.LanguageAPI;
import com.liferay.portal.model.User;
import dev.streamx.dotcms.connector.api.PublicationHandler;
import dev.streamx.dotcms.connector.api.StreamxChannel;
import dev.streamx.dotcms.connector.api.StreamxPublisher;
import dev.streamx.dotcms.connector.api.StreamxPushSummary;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

import static com.dotmarketing.util.Constants.USER_AGENT_DOTCMS_BROWSER;

/**
 * Handles dotCMS Content Types which are rendered through a detail page and forwards them to StreamX.
 */
public class UrlMappedPagePublicationHandler implements PublicationHandler {

    private final StreamxPublisher streamxPublisher;

    public UrlMappedPagePublicationHandler(final StreamxPublisher streamxPublisher) {
        this.streamxPublisher = streamxPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(@Nonnull Contentlet contentlet) {
        return contentlet.getContentType() instanceof UrlMapable && StringUtils.isNotBlank(contentlet.getStructure().getDetailPage());
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public List<StreamxPushSummary> handle(@Nonnull Contentlet contentlet) {
        try {
            final String uri = APILocator.getContentletAPI().getUrlMapForContentlet(contentlet, APILocator.getUserAPI().getSystemUser(), true);
            final String htmlContent = generateDetailPageHtml(contentlet);

            return List.of(streamxPublisher.pushPage(uri, htmlContent));
        } catch (final Exception exc) {
            return List.of(StreamxPushSummary.publicationError("N/A", StreamxChannel.PAGES, exc.getMessage()));
        }
    }

    private String generateDetailPageHtml(final Contentlet contentletToRender) throws DotDataException, DotSecurityException {
        final HTMLPageAssetAPI htmlPageAssetApi = APILocator.getHTMLPageAssetAPI();
        final ContentletAPI contentletApi = APILocator.getContentletAPI();
        final String detailPageId = contentletToRender.getStructure().getDetailPage();
        final User systemUser = APILocator.getUserAPI().getSystemUser();
        final long defaultLanguageId = APILocator.getLanguageAPI().getDefaultLanguage().getId();

        final Contentlet detailPageContentlet = contentletApi.findContentletByIdentifier(detailPageId, true, defaultLanguageId, systemUser, false);
        final IHTMLPage htmlPage = htmlPageAssetApi.fromContentlet(detailPageContentlet);

        return htmlPageAssetApi.getHTML(htmlPage, true, contentletToRender.getInode(),
                systemUser, defaultLanguageId, USER_AGENT_DOTCMS_BROWSER);
    }
}
