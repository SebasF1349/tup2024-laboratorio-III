package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class MovimientoRequestDto {
  @Schema(
      description = "Monto del movimiento",
      example = "1000",
      requiredMode = RequiredMode.REQUIRED)
  @Positive(message = "Campo debe ser un numero positivo")
  private double monto;

  @Schema(
      description = "Moneda del movimiento",
      example = "pesos",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "Campo no puede estar vacio")
  private String moneda;

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public String getMoneda() {
    return moneda;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    MovimientoRequestDto other = (MovimientoRequestDto) obj;
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

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  @Override
  public String toString() {
    return "MovimientoRequestDto{monto=" + monto + ", moneda=" + moneda + "}";
  }
}
