package dev.streamx.service;

import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.NodeNameHelper;
import javax.inject.Inject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class Product {

  @Inject
  DamTemplatingFunctions damFunctions;

  @Inject
  NodeNameHelper nodeNameHelper;

  private String title;

  private String description;

  private String color;

  private String imageUUID;

  private Long price;

  private String identifier;

  private String detailPagePath;

  private String renderedPageParent;

  public Asset getAsset() {
    return damFunctions.getAsset(imageUUID);
  }

  public String getNodeName() {
    return nodeNameHelper.getValidatedName(title);
  }
}
