package com.fancysoft.api.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GiphyResponse {

  private List<GiphyData> data;

  @Builder
  @Data
  public static class GiphyData {

    private String id;
  }
}
