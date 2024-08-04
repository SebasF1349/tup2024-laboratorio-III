package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.Positive;

public class TransferenciaResponseDto extends MovimientoResponseDto {
  @Positive private long cuentaOrigen;
  @Positive private long cuentaDestino;

  public TransferenciaResponseDto() {
    super();
    this.setTipoTransaccion("Transferencia");
  }

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
    TransferenciaResponseDto other = (TransferenciaResponseDto) obj;
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
    return "TransferenciaResponseDto{cuentaOrigen="
        + cuentaOrigen
        + ", cuentaDestino="
        + cuentaDestino
        + ", getFecha()="
        + getFecha()
        + ", getTipoTransaccion()="
        + getTipoTransaccion()
        + ", getDescripcion()="
        + getDescripcion()
        + ", getMonto()="
        + getMonto()
        + ", getMontoDebitado()="
        + getMontoDebitado()
        + "}";
  }
}
