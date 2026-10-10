package com.reportesciudadanos.dto.response;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class EventoContextoResponse {
  @com.fasterxml.jackson.annotation.JsonProperty Long id;
  @com.fasterxml.jackson.annotation.JsonProperty String fuente;
  @com.fasterxml.jackson.annotation.JsonProperty java.time.LocalDateTime instante;
  @com.fasterxml.jackson.annotation.JsonProperty String variable;
  @com.fasterxml.jackson.annotation.JsonProperty String unidad;
  @com.fasterxml.jackson.annotation.JsonProperty java.math.BigDecimal valor;
  @com.fasterxml.jackson.annotation.JsonProperty String interpretacion;}
