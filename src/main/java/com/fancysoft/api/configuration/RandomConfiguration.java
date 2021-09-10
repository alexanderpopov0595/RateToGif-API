package com.fancysoft.api.configuration;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomConfiguration {

  @Bean
  public Random rand() {
    return new Random();
  }
}
