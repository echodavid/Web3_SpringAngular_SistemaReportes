package com.reportesciudadanos.dto.response;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class SeguimientoResponse {
  @com.fasterxml.jackson.annotation.JsonProperty Long id;
  @com.fasterxml.jackson.annotation.JsonProperty com.reportesciudadanos.enums.EstadoReporte estadoAnterior;
  @com.fasterxml.jackson.annotation.JsonProperty com.reportesciudadanos.enums.EstadoReporte estadoNuevo;
  @com.fasterxml.jackson.annotation.JsonProperty String comentario;
  @com.fasterxml.jackson.annotation.JsonProperty String actorRol;
  @com.fasterxml.jackson.annotation.JsonProperty java.time.LocalDateTime fecha;}
