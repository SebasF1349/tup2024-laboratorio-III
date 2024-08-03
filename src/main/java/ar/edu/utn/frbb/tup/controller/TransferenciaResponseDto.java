package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class TransferenciaResponseDto {
  @Positive private long movimientoId;
  @NotEmpty private String diaHora;
  @Positive private long cuentaOrigen;
  @Positive private long cuentaDestino;
  @Positive private double monto;
  @NotEmpty private String moneda;
  @Positive private double montoDebitado;
  @NotEmpty private String descripcion;

  public long getMovimientoId() {
    return movimientoId;
  }

  public void setMovimientoId(long movimientoId) {
    this.movimientoId = movimientoId;
  }

  public String getDiaHora() {
    return diaHora;
  }

  public void setDiaHora(String diaHora) {
    this.diaHora = diaHora;
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

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public double getMontoDebitado() {
    return montoDebitado;
  }

  public void setMontoDebitado(double montoDebitado) {
    this.montoDebitado = montoDebitado;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (movimientoId ^ (movimientoId >>> 32));
    result = prime * result + ((diaHora == null) ? 0 : diaHora.hashCode());
    result = prime * result + (int) (cuentaOrigen ^ (cuentaOrigen >>> 32));
    result = prime * result + (int) (cuentaDestino ^ (cuentaDestino >>> 32));
    long temp;
    temp = Double.doubleToLongBits(monto);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((moneda == null) ? 0 : moneda.hashCode());
    temp = Double.doubleToLongBits(montoDebitado);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
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
    TransferenciaResponseDto other = (TransferenciaResponseDto) obj;
    if (movimientoId != other.movimientoId) {
      return false;
    }
    if (diaHora == null) {
      if (other.diaHora != null) {
        return false;
      }
    } else if (!diaHora.equals(other.diaHora)) {
      return false;
    }
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
    if (Double.doubleToLongBits(montoDebitado) != Double.doubleToLongBits(other.montoDebitado)) {
      return false;
    }
    if (descripcion == null) {
      if (other.descripcion != null) {
        return false;
      }
    } else if (!descripcion.equals(other.descripcion)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "TransferenciaResponseDto{movimientoId="
        + movimientoId
        + ", diaHora="
        + diaHora
        + ", cuentaOrigen="
        + cuentaOrigen
        + ", cuentaDestino="
        + cuentaDestino
        + ", monto="
        + monto
        + ", moneda="
        + moneda
        + ", montoDebitado="
        + montoDebitado
        + ", descripcion="
        + descripcion
        + "}";
  }
}
