package com.reportesciudadanos.dto.request;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class LoginRequest {
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Email String correoElectronico;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 72) String password;
  @Override
  public String toString() {
    return "LoginRequest[REDACTED]";
  }
}
