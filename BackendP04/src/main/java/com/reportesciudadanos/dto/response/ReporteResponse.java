package com.reportesciudadanos.dto.response;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class ReporteResponse {
  @com.fasterxml.jackson.annotation.JsonProperty Long id;
  @com.fasterxml.jackson.annotation.JsonProperty Long categoriaId;
  @com.fasterxml.jackson.annotation.JsonProperty Long zonaId;
  @com.fasterxml.jackson.annotation.JsonProperty String titulo;
  @com.fasterxml.jackson.annotation.JsonProperty String descripcion;
  @com.fasterxml.jackson.annotation.JsonProperty com.reportesciudadanos.enums.EstadoReporte estado;
  @com.fasterxml.jackson.annotation.JsonProperty com.reportesciudadanos.enums.Prioridad prioridad;
  @com.fasterxml.jackson.annotation.JsonProperty java.time.LocalDateTime fechaCreacion;
  @com.fasterxml.jackson.annotation.JsonProperty java.time.LocalDateTime fechaActualizacion;}
