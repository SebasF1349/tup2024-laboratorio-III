package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Positive;

public class TransferenciaRequestDto extends MovimientoRequestDto {
  @Schema(
      description = "Id de la Cuenta de Origen de la Transferencia",
      example = "123456",
      requiredMode = RequiredMode.REQUIRED)
  @Positive
  long cuentaOrigen;

  @Schema(
      description = "Id de la Cuenta de Destino de la Transferencia",
      example = "654321",
      requiredMode = RequiredMode.REQUIRED)
  @Positive
  long cuentaDestino;

  public long getCuentaOrigen() {
    return cuentaOrigen;
  }

  public void setCuentaOrigen(long cuentaOrigen) {
    this.cuentaOrigen = cuentaOrigen;
  }

  public long getCuentaDestino() {
    return cuentaDestino;
  }

  public void setCuentaDestino(long cuentaDestino) {
    this.cuentaDestino = cuentaDestino;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (int) (cuentaOrigen ^ (cuentaOrigen >>> 32));
    result = prime * result + (int) (cuentaDestino ^ (cuentaDestino >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TransferenciaRequestDto other = (TransferenciaRequestDto) obj;
    if (cuentaOrigen != other.cuentaOrigen) {
      return false;
    }
    if (cuentaDestino != other.cuentaDestino) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "TransferenciaRequestDto{cuentaOrigen="
        + cuentaOrigen
        + ", cuentaDestino="
        + cuentaDestino
        + ", getMonto()="
        + getMonto()
        + ", getMoneda()="
        + getMoneda()
        + "}";
  }
}
