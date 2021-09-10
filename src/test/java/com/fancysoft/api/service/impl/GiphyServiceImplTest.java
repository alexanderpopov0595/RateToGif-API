package com.fancysoft.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fancysoft.api.client.GiphyClient;
import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.model.GiphyResponse;
import com.fancysoft.api.model.GiphyResponse.GiphyData;
import feign.FeignException;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GiphyServiceImplTest {

  @Mock
  private GiphyClient client;
  @Mock
  private Random rand;
  @InjectMocks
  private GiphyServiceImpl service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "rateUpQuery", "UP");
    ReflectionTestUtils.setField(service, "rateDownQuery", "DOWN");
    ReflectionTestUtils.setField(service, "maxRandomOffset", 10);
    ReflectionTestUtils.setField(service, "imageUrlFormat", "http://%.com");
  }

  @Test
  void shouldReturnRandomGifIdWhenRateHasIncreased() {
    String expected = "http://1.com";

    GiphyResponse response = GiphyResponse.builder()
        .data(List.of(GiphyData.builder()
            .id("1")
            .build()))
        .build();

    when(client.searchGifsForQuery(eq("UP"), anyInt())).thenReturn(response);
    when(rand.nextInt(anyInt())).thenReturn(1);

    String actual = service.getRandomGifUrl(true);

    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnRandomGifIdWhenRateHasDecreased() {
    String expected = "http://1.com";

    GiphyResponse response = GiphyResponse.builder()
        .data(List.of(GiphyData.builder()
            .id("1")
            .build()))
        .build();

    when(client.searchGifsForQuery(eq("DOWN"), anyInt())).thenReturn(response);
    when(rand.nextInt(anyInt())).thenReturn(1);

    String actual = service.getRandomGifUrl(false);

    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowExceptionWhenGiphyCallIsFailed() {
    when(client.searchGifsForQuery(anyString(), anyInt())).thenThrow(FeignException.class);
    when(rand.nextInt(anyInt())).thenReturn(1);

    assertThrows(ApiException.class, () -> service.getRandomGifUrl(true));
  }

  @Test
  void shouldThrowExceptionWhenGiphyResponseIsEmpty() {
    GiphyResponse response = GiphyResponse.builder()
        .build();

    when(client.searchGifsForQuery(anyString(), anyInt())).thenReturn(response);
    when(rand.nextInt(anyInt())).thenReturn(1);

    ApiException exception = assertThrows(ApiException.class, () -> service.getRandomGifUrl(true));

    assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
    assertEquals("No gifs found for query", exception.getMessage());
  }
}
