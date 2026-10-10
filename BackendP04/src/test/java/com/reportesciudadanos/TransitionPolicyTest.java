package com.reportesciudadanos;

import static org.junit.jupiter.api.Assertions.*;

import com.reportesciudadanos.enums.EstadoReporte;
import com.reportesciudadanos.enums.RoleEnum;
import com.reportesciudadanos.exception.ApiException;
import com.reportesciudadanos.validation.TransitionPolicy;
import org.junit.jupiter.api.Test;

class TransitionPolicyTest {
  @Test
  void exhaustiveTransitionMatrix() {
    var policy = new TransitionPolicy();
    for (var from : EstadoReporte.values())
      for (var to : EstadoReporte.values())
        for (var role : RoleEnum.values()) {
          boolean edge = false;
          switch (from) {
            case REGISTRADO: edge = to == EstadoReporte.EN_REVISION; break;
            case EN_REVISION: edge = to == EstadoReporte.REGISTRADO || to == EstadoReporte.EN_ATENCION; break;
            case EN_ATENCION: edge = to == EstadoReporte.EN_REVISION || to == EstadoReporte.RESUELTO; break;
            case RESUELTO: edge = to == EstadoReporte.EN_ATENCION || to == EstadoReporte.CERRADO; break;
            default: break;
          }
          boolean allowed =
              edge
                  && role != RoleEnum.CIUDADANO
                  && ((role == RoleEnum.OPERADOR)
                      == (from == EstadoReporte.REGISTRADO || from == EstadoReporte.EN_REVISION));
          if (allowed) assertDoesNotThrow(() -> policy.validate(from, to, role));
          else assertThrows(ApiException.class, () -> policy.validate(from, to, role));
        }
  }
}
