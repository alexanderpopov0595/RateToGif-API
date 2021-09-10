package com.fancysoft.api.service;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface StreamingService {

  StreamingResponseBody getGifAsStream(String gifUrl);

}
