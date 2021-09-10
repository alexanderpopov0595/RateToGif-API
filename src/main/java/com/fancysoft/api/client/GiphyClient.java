package com.fancysoft.api.client;

import com.fancysoft.api.model.GiphyResponse;
import feign.Param;
import feign.RequestLine;

public interface GiphyClient {

  @RequestLine("GET /gifs/search?q={query}&offset={offset}")
  GiphyResponse searchGifsForQuery(@Param("query") String query, @Param int offset);

}
