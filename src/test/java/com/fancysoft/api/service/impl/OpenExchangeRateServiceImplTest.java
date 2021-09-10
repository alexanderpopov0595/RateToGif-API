package com.fancysoft.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fancysoft.api.client.OpenExchangeRatesClient;
import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.model.OpenExchangeRatesResponse;
import feign.FeignException;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class OpenExchangeRateServiceImplTest {

  @Mock
  private OpenExchangeRatesClient client;
  @InjectMocks
  private OpenExchangeRatesServiceImpl service;

  @Test
  void shouldReturnTrueWhenRateWentUp() {
    OpenExchangeRatesResponse todayResponse = OpenExchangeRatesResponse.builder()
        .base("BASE")
        .rates(Map.of("BASE", 1.0, "API_BASE", 10.0, "REQUESTED_CURRENCY", 5.0))
        .build();

    OpenExchangeRatesResponse yesterdayResponse = OpenExchangeRatesResponse.builder()
        .base("BASE")
        .rates(Map.of("BASE", 1.0, "API_BASE", 10.0, "REQUESTED_CURRENCY", 2.0))
        .build();

    when(client.getHistoricalRates(any(LocalDate.class)))
        .thenReturn(todayResponse)
        .thenReturn(yesterdayResponse);

    assertTrue(service.hasRateIncreased("REQUESTED_CURRENCY"));
  }

  @Test
  void shouldReturnTrueWhenRateWentDown() {
    OpenExchangeRatesResponse todayResponse = OpenExchangeRatesResponse.builder()
        .base("BASE")
        .rates(Map.of("BASE", 1.0, "API_BASE", 10.0, "REQUESTED_CURRENCY", 2.0))
        .build();

    OpenExchangeRatesResponse yesterdayResponse = OpenExchangeRatesResponse.builder()
        .base("BASE")
        .rates(Map.of("BASE", 1.0, "API_BASE", 10.0, "REQUESTED_CURRENCY", 5.0))
        .build();

    when(client.getHistoricalRates(any(LocalDate.class)))
        .thenReturn(todayResponse)
        .thenReturn(yesterdayResponse);

    assertFalse(service.hasRateIncreased("REQUESTED_CURRENCY"));
  }

  @Test
  void shouldThrowExceptionWhenOpenExchangesRateCallIsFailed() {
    when(client.getHistoricalRates(any(LocalDate.class))).thenThrow(FeignException.class);

    assertThrows(ApiException.class, () -> service.hasRateIncreased("REQUESTED_CURRENCY"));
  }

  @Test
  void shouldThrowExceptionWhenCurrencyDoesNotFound() {
    OpenExchangeRatesResponse todayResponse = OpenExchangeRatesResponse.builder()
        .base("BASE")
        .rates(Map.of("BASE", 1.0, "API_BASE", 10.0))
        .build();

    OpenExchangeRatesResponse yesterdayResponse = OpenExchangeRatesResponse.builder()
        .base("BASE")
        .rates(Map.of("BASE", 1.0, "API_BASE", 10.0))
        .build();

    when(client.getHistoricalRates(any(LocalDate.class)))
        .thenReturn(todayResponse)
        .thenReturn(yesterdayResponse);

    ApiException exception = assertThrows(ApiException.class, () -> service.hasRateIncreased("REQUESTED_CURRENCY"));

    assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatus());
    assertEquals("Rate is not found for requested currency REQUESTED_CURRENCY", exception.getMessage());
  }
}
