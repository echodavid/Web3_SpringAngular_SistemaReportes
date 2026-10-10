package com.reportesciudadanos.dto.request;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class RegisterRequest {
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 150) String nombre;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Email @Size(max = 254) String correoElectronico;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(min = 12, max = 72) String password;
  @Override
  public String toString() {
    return "RegisterRequest[REDACTED]";
  }
}
