package dev.streamx.service;

import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.node2bean.Node2BeanException;
import info.magnolia.jcr.node2bean.Node2BeanProcessor;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Singleton
public class ArticleService {

  private final TemplateTypeHelper templateTypeHelper;

  private final Node2BeanProcessor beanProcessor;

  private static final String DEFAULT_ARTICLE_NAME = "Title-of-the-blog-post";

  @Inject
  public ArticleService(TemplateTypeHelper templateTypeHelper, Node2BeanProcessor beanProcessor) {
    this.templateTypeHelper = templateTypeHelper;
    this.beanProcessor = beanProcessor;
  }

  public List<Article> getArticles() {
    List<Article> articles = new LinkedList<>();
    try {
      Session session = MgnlContext.getJCRSession("articles");
      List<Node> articleNodes = this.templateTypeHelper.getContentListByTemplateIds(
          session.getRootNode(), null, Integer.MAX_VALUE, null, null);
      for (Node articleNode : articleNodes) {
        Article article = this.marshallArticleNode(articleNode);
        articles.add(article);
      }
    } catch (RepositoryException e) {
      log.error("Could not get articles.", e);
    }

    return articles;
  }

  public Node getArticleNodeByParameter() throws RepositoryException {
    String article = StringUtils.defaultIfBlank(MgnlContext.getParameter("article"), MgnlContext.getAttribute("item"));
    article = StringUtils.defaultIfBlank(article, DEFAULT_ARTICLE_NAME);
    return this.getContentNodeByName(article, "articles");
  }

  private Node getContentNodeByName(String pathOrName, String workspace) throws RepositoryException {
    if (pathOrName.startsWith("/")) {
      return MgnlContext.getJCRSession(workspace).getNode(StringUtils.substringBefore(pathOrName, "?"));
    } else {
      String sql = String.format("SELECT * FROM [nt:base] AS content WHERE name(content)='%s'", pathOrName);
      NodeIterator items = QueryUtil.search(workspace, sql, "JCR-SQL2", "mgnl:content");
      if (items.hasNext()) {
        return items.nextNode();
      } else {
        log.warn("Could not find node from workspace [{}] based on name [{}]", workspace, pathOrName);
        return null;
      }
    }
  }

  public Article marshallArticleNode(Node articleNode) {
    Article article = null;
    if (articleNode != null) {
      article = new Article();
      try {
        article = (Article) beanProcessor.toBean(articleNode, Article.class);
      } catch (RepositoryException | Node2BeanException e) {
        log.warn("Could not marshall article from node [{}]", articleNode, e);
      }
    }

    return article;
  }

}
