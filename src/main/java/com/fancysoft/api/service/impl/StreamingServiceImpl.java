package com.fancysoft.api.service.impl;

import com.fancysoft.api.service.StreamingService;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.URL;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@Service
public class StreamingServiceImpl implements StreamingService {

  @Override
  public StreamingResponseBody getGifAsStream(String gifUrl) {
    return outputStream -> {
      try(InputStream is = new URL(gifUrl).openStream()) {
        is.transferTo(outputStream);
      }
    };
  }
}
