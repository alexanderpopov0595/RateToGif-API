package com.fancysoft.api.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenExchangeRatesResponse {

  private String disclaimer;
  private String license;
  private long timestamp;
  private String base;
  private Map<String, Double> rates;

}
