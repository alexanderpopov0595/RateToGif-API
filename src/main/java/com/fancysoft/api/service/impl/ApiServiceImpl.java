package com.fancysoft.api.service.impl;

import com.fancysoft.api.service.GiphyService;
import com.fancysoft.api.service.ApiService;
import com.fancysoft.api.service.OpenExchangeRatesService;
import com.fancysoft.api.service.StreamingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiServiceImpl implements ApiService {

  private final OpenExchangeRatesService rateService;
  private final GiphyService giphyService;
  private final StreamingService streamingService;

  @Override
  public StreamingResponseBody getGifForRate(String currency) {
    log.info("Looking for a rate for currency {}", currency);
    boolean hasRateIncreased = rateService.hasRateIncreased(currency);
    log.info("Rate has change: {}", hasRateIncreased);
    String gifUrl = giphyService.getRandomGifUrl(hasRateIncreased);
    log.info("Downloading gif from url: {}", gifUrl);
    return streamingService.getGifAsStream(gifUrl);
  }

}
