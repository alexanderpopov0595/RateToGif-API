package com.fancysoft.api.controller;

import com.fancysoft.api.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RequiredArgsConstructor
@RestController
public class ApiController {

  private final ApiService service;

  @GetMapping("/rate/{currency}")
  public ResponseEntity<StreamingResponseBody> getGifForRate(@PathVariable("currency") String currency) {
    return ResponseEntity
        .ok()
        .contentType(MediaType.IMAGE_GIF)
        .cacheControl(CacheControl.noCache())
        .body(service.getGifForRate(currency));
  }
}
