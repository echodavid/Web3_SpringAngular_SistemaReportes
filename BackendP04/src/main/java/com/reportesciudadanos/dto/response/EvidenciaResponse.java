package com.reportesciudadanos.dto.response;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class EvidenciaResponse {
  @com.fasterxml.jackson.annotation.JsonProperty Long id;
  @com.fasterxml.jackson.annotation.JsonProperty String tipo;
  @com.fasterxml.jackson.annotation.JsonProperty String referenciaFicticia;
  @com.fasterxml.jackson.annotation.JsonProperty String descripcion;
  @com.fasterxml.jackson.annotation.JsonProperty java.time.LocalDateTime fechaRegistro;}
