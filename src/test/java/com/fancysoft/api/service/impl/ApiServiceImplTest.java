package com.fancysoft.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.service.GiphyService;
import com.fancysoft.api.service.OpenExchangeRatesService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@ExtendWith(MockitoExtension.class)
class ApiServiceImplTest {

  @Mock
  private OpenExchangeRatesService ratesService;
  @Mock
  private GiphyService giphyService;
  @Mock
  private StreamingServiceImpl streamingService;
  @InjectMocks
  private ApiServiceImpl service;

  @Test
  void shouldReturnGif() throws IOException {
    when(ratesService.hasRateIncreased(anyString())).thenReturn(true);
    when(giphyService.getRandomGifUrl(anyBoolean())).thenReturn("url");
    when(streamingService.getGifAsStream((anyString()))).thenReturn(outputStream -> outputStream.write("GIF".getBytes(
        StandardCharsets.UTF_8)));

    StreamingResponseBody actual = service.getGifForRate("USD");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    actual.writeTo(outputStream);
    assertEquals("GIF", outputStream.toString(StandardCharsets.UTF_8));
  }

  @Test
  void shouldThrowExceptionWhenOpenExchangeRatesServiceCallIsFailed() {
    when(ratesService.hasRateIncreased(anyString())).
        thenThrow(new ApiException(HttpStatus.SERVICE_UNAVAILABLE.value(), "Some error description"));

    ApiException exception = assertThrows(ApiException.class, () -> service.getGifForRate("USD"));

    assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), exception.getStatus());
    assertEquals("Some error description", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenGiphyServiceCallIsFailed() {
    when(ratesService.hasRateIncreased(anyString())).thenReturn(true);
    when(giphyService.getRandomGifUrl(anyBoolean())).
        thenThrow(new ApiException(HttpStatus.SERVICE_UNAVAILABLE.value(), "Some error description"));

    ApiException exception = assertThrows(ApiException.class, () -> service.getGifForRate("USD"));

    assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), exception.getStatus());
    assertEquals("Some error description", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenGifLoaderServiceCallIsFailed() {
    when(ratesService.hasRateIncreased(anyString())).thenReturn(true);
    when(giphyService.getRandomGifUrl(anyBoolean())).thenReturn("url");
    when(streamingService.getGifAsStream(anyString())).
        thenThrow(new ApiException(HttpStatus.SERVICE_UNAVAILABLE.value(), "Some error description"));

    ApiException exception = assertThrows(ApiException.class, () -> service.getGifForRate("USD"));

    assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), exception.getStatus());
    assertEquals("Some error description", exception.getMessage());
  }

}

