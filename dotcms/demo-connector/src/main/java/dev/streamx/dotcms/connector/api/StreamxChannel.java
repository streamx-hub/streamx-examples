package dev.streamx.dotcms.connector.api;

import lombok.Getter;

/**
 * Classification of StreamX channels where different type of content is pushed to.
 */
@Getter
public enum StreamxChannel {
    PAGES("pages"),
    ASSETS("assets"),
    WEB_RESOURCES("web-resources");

    private final String name;

    StreamxChannel(final String name) {
        this.name = name;
    }
}
