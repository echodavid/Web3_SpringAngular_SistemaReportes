package com.reportesciudadanos.dto.request;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class EvidenciaCreateRequest {
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 50) String tipo;
  @com.fasterxml.jackson.annotation.JsonProperty @NotBlank @Size(max = 255) String referenciaFicticia;
  @com.fasterxml.jackson.annotation.JsonProperty @Size(max = 255) String descripcion;}
