package dev.streamx.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.DeltaBuilder;

public class DemoContentMagnoliaVersionHandler extends DefaultModuleVersionHandler {
  
  public DemoContentMagnoliaVersionHandler() {
    this.register(DeltaBuilder.update("1.0", "")
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/dam.streamx-demo.xml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/website.article.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/website.blog.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/website.index.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/website.product.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/website.products.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/resources.streamx..css.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/products.Basic-Tee.yaml"))
        .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap/demo-content-magnolia/articles.Title-of-the-blog-post.yaml"))
    );
  }
}
