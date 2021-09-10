package com.fancysoft.api.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.model.ApiError;
import com.fancysoft.api.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApiControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ApiService service;

  private final ObjectMapper mapper = new JsonMapper();

  @Test
  void shouldReturnGif() throws Exception {
    when(service.getGifForRate(anyString())).thenReturn(outputStream -> outputStream.write("GIF".getBytes(
        StandardCharsets.UTF_8)));

    mockMvc.perform(get("/rate/USD"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.IMAGE_GIF))
        .andExpect(content().string("GIF"));
  }

  @Test
  void shouldReturn503WhenDownstreamServiceIsUnavailable() throws Exception {
    ApiError error = ApiError.builder()
        .status(HttpStatus.SERVICE_UNAVAILABLE.value())
        .details("Downstream service is unavailable")
        .build();

    when(service.getGifForRate(anyString())).thenThrow(new ApiException(HttpStatus.SERVICE_UNAVAILABLE
        .value(), "Downstream service is unavailable"));

    mockMvc.perform(get("/rate/USD"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(mapper.writeValueAsString(error)))
        .andExpect(status().isServiceUnavailable());
  }

  @Test
  void shouldReturn400WhenRequestIsInvalid() throws Exception {
    ApiError error = ApiError.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .details("Request is invalid")
        .build();

    when(service.getGifForRate(anyString())).thenThrow(new ApiException(HttpStatus.BAD_REQUEST
        .value(), "Request is invalid"));

    mockMvc.perform(get("/rate/USD"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(mapper.writeValueAsString(error)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn500WhenSomeInternalErrorHappened() throws Exception {
    ApiError error = ApiError.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .details("Something went wrong")
        .build();

    when(service.getGifForRate(anyString())).thenThrow(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR
        .value(), "Something went wrong"));

    mockMvc.perform(get("/rate/USD"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(mapper.writeValueAsString(error)))
        .andExpect(status().isInternalServerError());
  }
}
