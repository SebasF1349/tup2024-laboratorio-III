package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotNull;

public class MovimientoResponseDto {
  @NotNull private String fecha;
  @NotNull private String tipoTransaccion;
  @NotNull private String descripcion;
  @NotNull private double monto;
  @NotNull private double montoDebitado;

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getTipoTransaccion() {
    return tipoTransaccion;
  }

  public void setTipoTransaccion(String tipoTransaccion) {
    this.tipoTransaccion = tipoTransaccion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public double getMontoDebitado() {
    return montoDebitado;
  }

  public void setMontoDebitado(double montoDebitado) {
    this.montoDebitado = montoDebitado;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
    result = prime * result + ((tipoTransaccion == null) ? 0 : tipoTransaccion.hashCode());
    result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
    long temp;
    temp = Double.doubleToLongBits(monto);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    MovimientoResponseDto other = (MovimientoResponseDto) obj;
    if (fecha == null) {
      if (other.fecha != null) {
        return false;
      }
    } else if (!fecha.equals(other.fecha)) {
      return false;
    }
    if (tipoTransaccion == null) {
      if (other.tipoTransaccion != null) {
        return false;
      }
    } else if (!tipoTransaccion.equals(other.tipoTransaccion)) {
      return false;
    }
    if (descripcion == null) {
      if (other.descripcion != null) {
        return false;
      }
    } else if (!descripcion.equals(other.descripcion)) {
      return false;
    }
    if (Double.doubleToLongBits(monto) != Double.doubleToLongBits(other.monto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MovimientoResponseDto{fecha="
        + fecha
        + ", tipoTransaccion="
        + tipoTransaccion
        + ", descripcion="
        + descripcion
        + ", monto="
        + monto
        + "}";
  }
}
