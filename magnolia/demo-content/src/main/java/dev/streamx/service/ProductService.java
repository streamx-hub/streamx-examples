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
public class ProductService {

  private final TemplateTypeHelper templateTypeHelper;

  private final Node2BeanProcessor beanProcessor;

  private static final String DEFAULT_PRODUCT_NAME = "Basic-Tee";

  @Inject
  public ProductService(TemplateTypeHelper templateTypeHelper, Node2BeanProcessor beanProcessor) {
    this.templateTypeHelper = templateTypeHelper;
    this.beanProcessor = beanProcessor;
  }

  public List<Product> getProducts() {
    List<Product> products = new LinkedList<>();
    try {
      Session session = MgnlContext.getJCRSession("products");
      List<Node> productNodes = this.templateTypeHelper.getContentListByTemplateIds(
          session.getRootNode(), null, Integer.MAX_VALUE, null, null);
      for (Node productNode : productNodes) {
        Product product = this.marshallProductNode(productNode);
        products.add(product);
      }
    } catch (RepositoryException e) {
      log.error("Could not get products.", e);
    }

    return products;
  }

  public Node getProductNodeByParameter() throws RepositoryException {
    String product = StringUtils.defaultIfBlank(MgnlContext.getParameter("product"), MgnlContext.getAttribute("item"));
    product = StringUtils.defaultIfBlank(product, DEFAULT_PRODUCT_NAME);
    return this.getContentNodeByName(product, "products");
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

  public Product marshallProductNode(Node productNode) {
    Product product = null;
    if (productNode != null) {
      product = new Product();
      try {
        product = (Product) beanProcessor.toBean(productNode, Product.class);
      } catch (RepositoryException | Node2BeanException e) {
        log.warn("Could not marshall product from node [{}]", productNode, e);
      }
    }

    return product;
  }

}
