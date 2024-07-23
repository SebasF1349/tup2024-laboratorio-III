package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class TransferenciaDto {
  @Positive long cuentaOrigen;
  @Positive long cuentaDestino;
  @Positive double monto;
  @NotEmpty String moneda;

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

  public double getMonto() {
    return monto;
  }

  public void setMonto(double mont) {
    this.monto = mont;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (cuentaOrigen ^ (cuentaOrigen >>> 32));
    result = prime * result + (int) (cuentaDestino ^ (cuentaDestino >>> 32));
    long temp;
    temp = Double.doubleToLongBits(monto);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((moneda == null) ? 0 : moneda.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TransferenciaDto other = (TransferenciaDto) obj;
    if (cuentaOrigen != other.cuentaOrigen) {
      return false;
    }
    if (cuentaDestino != other.cuentaDestino) {
      return false;
    }
    if (Double.doubleToLongBits(monto) != Double.doubleToLongBits(other.monto)) {
      return false;
    }
    if (moneda == null) {
      if (other.moneda != null) {
        return false;
      }
    } else if (!moneda.equals(other.moneda)) {
      return false;
    }
    return true;
  }
}
