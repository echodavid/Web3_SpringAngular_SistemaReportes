package com.reportesciudadanos.dto.response;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class PageResponse<T> {
  @com.fasterxml.jackson.annotation.JsonProperty java.util.List<T> content;
  @com.fasterxml.jackson.annotation.JsonProperty int page;
  @com.fasterxml.jackson.annotation.JsonProperty int size;
  @com.fasterxml.jackson.annotation.JsonProperty long totalElements;
  @com.fasterxml.jackson.annotation.JsonProperty int totalPages;
  public static <T> PageResponse<T> of(org.springframework.data.domain.Page<T> p) {
    return new PageResponse<>(
        p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
  }
}
