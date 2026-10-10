package com.reportesciudadanos.dto.request;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class ReporteCreateRequest {
  @com.fasterxml.jackson.annotation.JsonProperty @NotNull @Positive Long categoriaId;
  @com.fasterxml.jackson.annotation.JsonProperty @NotNull @Positive Long zonaId;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 200) String titulo;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 4000) String descripcion;
  @com.fasterxml.jackson.annotation.JsonProperty @NotNull com.reportesciudadanos.enums.Prioridad prioridad;}
