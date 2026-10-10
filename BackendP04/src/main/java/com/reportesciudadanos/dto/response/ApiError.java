package com.reportesciudadanos.dto.response;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class ApiError {
  @com.fasterxml.jackson.annotation.JsonProperty String code;
  @com.fasterxml.jackson.annotation.JsonProperty String message;
  @com.fasterxml.jackson.annotation.JsonProperty java.time.LocalDateTime timestamp;
  @com.fasterxml.jackson.annotation.JsonProperty String path;}
