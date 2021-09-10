package com.fancysoft.api.service.impl;

import com.fancysoft.api.client.GiphyClient;
import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.model.GiphyResponse;
import com.fancysoft.api.model.GiphyResponse.GiphyData;
import com.fancysoft.api.service.GiphyService;
import feign.FeignException;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GiphyServiceImpl implements GiphyService {

  private final GiphyClient client;
  private final Random rand;

  @Value("${client.giphy.rateUpQuery}")
  private String rateUpQuery;
  @Value("${client.giphy.rateDownQuery}")
  private String rateDownQuery;
  @Value("${client.giphy.maxRandomOffset}")
  private int maxRandomOffset;
  @Value("${client.giphy.imageUrlFormat}")
  private String imageUrlFormat;

  @Override
  public String getRandomGifUrl(boolean hasRateIncreased) {
    String query = getQueryForRate(hasRateIncreased);
    log.info("Search gif for query: {}", query);
    GiphyResponse response = getGifsForQuery(query);
    String gifId = getGifId(response);
    log.info("Gif id: {}", gifId);
    return getGifUrl(gifId);
  }

  private String getQueryForRate(boolean hasRateIncreased) {
    return hasRateIncreased ? rateUpQuery : rateDownQuery;
  }

  private GiphyResponse getGifsForQuery(String query) {
    try{
      return client.searchGifsForQuery(query, rand.nextInt(maxRandomOffset));
    }
    catch (FeignException e) {
      log.info("Unable to perform GET-request to Giphy backend: {}", e);
      throw new ApiException(e.status(), e.getMessage());
    }
  }

  private String getGifId(GiphyResponse response) {
    return Optional.ofNullable(response)
        .map(GiphyResponse::getData)
        .map(data -> data.isEmpty() ? null : data.get(0))
        .map(GiphyData::getId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "No gifs found for query"));
  }

  private String getGifUrl(String gifId) {
    return imageUrlFormat.replace("%", gifId);
  }

}
