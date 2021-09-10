package com.fancysoft.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fancysoft.api.service.StreamingService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

class StreamingServiceImplTest {

  private final StreamingService service = new StreamingServiceImpl();

  @Test
  void shouldReturnGifBytes() throws IOException {
    String expected = "GIF";

    URL.setURLStreamHandlerFactory(protocol -> "inmemory".equals(protocol) ? new URLStreamHandler() {
      @Override
      protected URLConnection openConnection(URL url) {
        return new URLConnection(url) {
          @Override
          public void connect() {

          }

          @Override
          public InputStream getInputStream() {
            return new ByteArrayInputStream("GIF".getBytes(StandardCharsets.UTF_8));
          }
        };
      }
    } : null);

    StreamingResponseBody response = service.getGifAsStream("inmemory:gif.con");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    response.writeTo(outputStream);

    String actual = outputStream.toString(StandardCharsets.UTF_8);

    assertEquals(expected, actual);
  }
}
