package dev.streamx.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.TemplateDefinition;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import dev.streamx.service.Product;
import dev.streamx.service.ProductService;

public class ProductDetailModel extends RenderingModelImpl<TemplateDefinition> {

  private final ProductService productService;

  @Inject
  public ProductDetailModel (Node content, TemplateDefinition definition,
      RenderingModel<?> parent, ProductService productService) {
    super(content, definition, parent);
    this.productService = productService;
  }

  public Product getProduct() throws RepositoryException {
    Node productNode = productService.getProductNodeByParameter();
    return productService.marshallProductNode(productNode);
  }

}
