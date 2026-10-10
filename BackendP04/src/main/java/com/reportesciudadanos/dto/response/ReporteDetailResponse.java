package com.reportesciudadanos.dto.response;

import javax.validation.constraints.*;

@lombok.Value
@lombok.experimental.Accessors(fluent = true)
@com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
public class ReporteDetailResponse {
  @com.fasterxml.jackson.annotation.JsonProperty ReporteResponse reporte;
  @com.fasterxml.jackson.annotation.JsonProperty java.util.List<com.reportesciudadanos.dto.response.EvidenciaResponse> evidencias;
  @com.fasterxml.jackson.annotation.JsonProperty java.util.List<com.reportesciudadanos.dto.response.SeguimientoResponse> seguimientos;
  @com.fasterxml.jackson.annotation.JsonProperty java.util.List<com.reportesciudadanos.dto.response.EventoContextoResponse> eventosContexto;}
