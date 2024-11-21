package dev.streamx.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.TemplateDefinition;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import dev.streamx.service.Product;
import dev.streamx.service.ProductService;

public class ProductsListModel extends RenderingModelImpl<TemplateDefinition> {

  private final ProductService productService;

  @Inject
  public ProductsListModel(Node content, TemplateDefinition definition,
      RenderingModel<?> parent, ProductService productService) {
    super(content, definition, parent);
    this.productService = productService;
  }

  public List<Product> getProducts() {
    return this.productService.getProducts();
  }
}
