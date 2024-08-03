package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.Positive;

public class TransferenciaRequestDto extends MovimientoRequestDto {
  @Positive long cuentaOrigen;
  @Positive long cuentaDestino;

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
