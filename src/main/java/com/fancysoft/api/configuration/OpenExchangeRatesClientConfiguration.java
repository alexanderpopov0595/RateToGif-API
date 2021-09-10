package com.fancysoft.api.configuration;

import com.fancysoft.api.client.OpenExchangeRatesClient;
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
public class OpenExchangeRatesClientConfiguration {

  @Value("${client.openExchangeRates.baseUrl}")
  private String baseUrl;
  @Value("${client.openExchangeRates.appId}")
  private String appId;
  @Value("${client.openExchangeRates.base}")
  private String base;

  @Bean
  public OpenExchangeRatesClient openExchangeRatesClient(@Qualifier("openExchangeRatesInterceptor") RequestInterceptor interceptor) {
    return Feign.builder()
        .client(new OkHttpClient())
        .encoder(new GsonEncoder())
        .decoder(new GsonDecoder())
        .requestInterceptor(interceptor)
        .logger(new Slf4jLogger(OpenExchangeRatesClient.class))
        .logLevel(Level.FULL)
        .target(OpenExchangeRatesClient.class, baseUrl);
  }

  @Bean(name = "openExchangeRatesInterceptor")
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("Content-Type", "application/json");
      requestTemplate.header("Authorization", "Token " + appId);
      requestTemplate.query("base", base);
    };
  }
}
