package dev.streamx.model;

import dev.streamx.service.ArticleService;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.TemplateDefinition;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import dev.streamx.service.Article;

public class ArticleDetailModel extends RenderingModelImpl<TemplateDefinition> {

  private final ArticleService articleService;

  @Inject
  public ArticleDetailModel(Node content, TemplateDefinition definition,
      RenderingModel<?> parent, ArticleService articleService) {
    super(content, definition, parent);
    this.articleService = articleService;
  }

  public Article getArticle() throws RepositoryException {
    Node articleNode = articleService.getArticleNodeByParameter();
    return articleService.marshallArticleNode(articleNode);
  }

}
