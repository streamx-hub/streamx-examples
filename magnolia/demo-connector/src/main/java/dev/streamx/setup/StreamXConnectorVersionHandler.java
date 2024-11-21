package dev.streamx.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.delta.BootstrapSingleModuleResource;
import info.magnolia.module.delta.DeltaBuilder;

public class StreamXConnectorVersionHandler extends DefaultModuleVersionHandler {

  public StreamXConnectorVersionHandler() {
    this.register(DeltaBuilder.update("1.0", "")
        .addTask(new BootstrapSingleModuleResource("config.modules.streamx-connector-magnolia.commands.xml")));
  }

}
