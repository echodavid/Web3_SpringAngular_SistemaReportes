package com.reportesciudadanos.dto.request;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class ReporteStatusRequest {
  @com.fasterxml.jackson.annotation.JsonProperty @NotNull com.reportesciudadanos.enums.EstadoReporte estado;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 2000) String comentario;}
