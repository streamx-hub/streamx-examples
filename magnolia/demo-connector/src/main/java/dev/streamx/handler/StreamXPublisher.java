package dev.streamx.handler;

import java.io.InputStream;
import javax.annotation.Nonnull;
import dev.streamx.util.StreamXPushSummary;

public interface StreamXPublisher {

  StreamXPushSummary pushPage(@Nonnull String relativePath, @Nonnull String html);

  StreamXPushSummary pushAsset(@Nonnull String relativePath, @Nonnull InputStream inputStream);

  StreamXPushSummary pushWebResource(@Nonnull String relativePath, @Nonnull InputStream inputStream);

}
