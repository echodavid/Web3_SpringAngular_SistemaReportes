package com.reportesciudadanos.dto.response;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class AuthResponse {
  @com.fasterxml.jackson.annotation.JsonProperty String accessToken;
  @com.fasterxml.jackson.annotation.JsonProperty String tokenType;
  @com.fasterxml.jackson.annotation.JsonProperty long expiresIn;}
