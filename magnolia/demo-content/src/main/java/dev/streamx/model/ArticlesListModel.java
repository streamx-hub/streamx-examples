package dev.streamx.model;

import dev.streamx.service.Article;
import dev.streamx.service.ArticleService;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.TemplateDefinition;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;

public class ArticlesListModel extends RenderingModelImpl<TemplateDefinition> {

  private final ArticleService articleService;

  @Inject
  public ArticlesListModel(Node content, TemplateDefinition definition,
      RenderingModel<?> parent, ArticleService articleService) {
    super(content, definition, parent);
    this.articleService = articleService;
  }

  public List<Article> getArticles() {
    return this.articleService.getArticles();
  }

}
