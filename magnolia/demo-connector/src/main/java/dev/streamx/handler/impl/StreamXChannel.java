package dev.streamx.handler.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum StreamXChannel {
  PAGES("pages"),
  ASSETS("assets"),
  WEB_RESOURCES("web-resources");

  private final String name;
}
