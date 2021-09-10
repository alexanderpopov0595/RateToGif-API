package com.fancysoft.api.service.impl;

import com.fancysoft.api.client.OpenExchangeRatesClient;
import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.model.OpenExchangeRatesResponse;
import com.fancysoft.api.service.OpenExchangeRatesService;
import feign.FeignException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenExchangeRatesServiceImpl implements OpenExchangeRatesService {

  private final OpenExchangeRatesClient client;

  @Override
  public boolean hasRateIncreased(String currency) {
    OpenExchangeRatesResponse todayResponse = getRateForDate(LocalDate.now());
    OpenExchangeRatesResponse yesterdayResponse = getRateForDate(LocalDate.now().minusDays(1));

    double todayRate = getRateForCurrency(todayResponse, currency);
    double yesterdayRate = getRateForCurrency(yesterdayResponse, currency);

    log.info("Comparing today rate {} and yesterday rate {}", todayRate, yesterdayRate);

    return todayRate > yesterdayRate;
  }

  private OpenExchangeRatesResponse getRateForDate(LocalDate date) {
    try{
      return client.getHistoricalRates(date);
    }
    catch (FeignException e) {
      log.error("Unable to perform GET-request to OpenExchangeRates backend: {}", e);
      throw new ApiException(e.status(), e.getMessage());
    }
  }

  private double getRateForCurrency(OpenExchangeRatesResponse response, String currency) {
    if(response.getRates().containsKey(currency)){
      return response.getRates().get(currency);
    }
    log.error("Rate is not found for requested currency {}", currency);
    throw new ApiException(HttpStatus.BAD_REQUEST.value(), String.format("Rate is not found for requested currency %s", currency));
  }
}
