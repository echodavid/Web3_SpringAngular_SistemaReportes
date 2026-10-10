package com.reportesciudadanos.dto.request;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class EventoContextoCreateRequest {
  @com.fasterxml.jackson.annotation.JsonProperty @NotNull @Positive Long reporteId;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 255) String fuente;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 255) String variable;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 255) String unidad;
  @com.fasterxml.jackson.annotation.JsonProperty @NotNull @Digits(integer = 13, fraction = 6) java.math.BigDecimal valor;
  @com.fasterxml.jackson.annotation.JsonProperty @Size(max = 255) String interpretacion;}
