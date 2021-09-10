package com.fancysoft.api.client;

import com.fancysoft.api.model.OpenExchangeRatesResponse;
import feign.Param;
import feign.RequestLine;
import java.time.LocalDate;

public interface OpenExchangeRatesClient {

  @RequestLine("GET /historical/{date}.json")
  OpenExchangeRatesResponse getHistoricalRates(@Param("date") LocalDate date);

}
