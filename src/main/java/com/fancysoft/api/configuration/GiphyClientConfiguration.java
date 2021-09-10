package com.fancysoft.api.configuration;

import com.fancysoft.api.client.GiphyClient;
import feign.Feign;
import feign.Logger.Level;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GiphyClientConfiguration {

  @Value("${client.giphy.baseUrl}")
  private String baseUrl;
  @Value("${client.giphy.apiKey}")
  private String apiKey;

  @Bean
  public GiphyClient giphyClient(@Qualifier("GiphyInterceptor") RequestInterceptor interceptor) {
    return Feign.builder()
        .client(new OkHttpClient())
        .encoder(new GsonEncoder())
        .decoder(new GsonDecoder())
        .requestInterceptor(interceptor)
        .logger(new Slf4jLogger(GiphyClient.class))
        .logLevel(Level.FULL)
        .target(GiphyClient.class, baseUrl);
  }

  @Bean(name = "GiphyInterceptor")
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.query("api_key", apiKey);
      requestTemplate.query("limit", "1");
      requestTemplate.header("Content-Type", "application/json");
    };
  }
}
