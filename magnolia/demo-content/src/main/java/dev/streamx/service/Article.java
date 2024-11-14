package dev.streamx.service;

import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.NodeNameHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.inject.Inject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class Article {

  @Inject
  DamTemplatingFunctions damFunctions;

  @Inject
  NodeNameHelper nodeNameHelper;

  private String title;

  private String content;

  private String imageUUID;

  private String author;

  private String jobTitle;

  private Calendar date;

  private String identifier;

  private String detailPagePath;

  private String renderedPageParent;

  public void setDate(String date) {
    this.date = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    try {
      this.date.setTime(format.parse(date));
    } catch (ParseException e) {
      log.info("Could not parse date value", e);
    }
  }

  public String getDate() {
    SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
    return format.format(date.getTime());
  }

  public Asset getAsset() {
    return damFunctions.getAsset(imageUUID);
  }

  public String getNodeName() {
    return nodeNameHelper.getValidatedName(title);
  }
}
