package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class CuentaRequestDto {
  @Schema(
      description = "Dinero disponible en la cuenta",
      example = "0",
      requiredMode = RequiredMode.REQUIRED)
  @PositiveOrZero(message = "Campo debe ser un numero positivo o cero")
  private double balance;

  @Schema(
      description = "Tipo de cuenta",
      example = "Caja de Ahorros",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "Campo no puede estar vacio")
  private String tipoCuenta;

  @Schema(description = "Moneda", example = "Pesos", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "Campo no puede estar vacio")
  private String moneda;

  @Schema(
      description = "DNI del Titular",
      example = "12345678",
      requiredMode = RequiredMode.REQUIRED)
  @Positive(message = "Campo debe ser un numero positivo")
  private long titular;

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public String getTipoCuenta() {
    return tipoCuenta;
  }

  public void setTipoCuenta(String tipoCuenta) {
    this.tipoCuenta = tipoCuenta;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public long getTitular() {
    return titular;
  }

  public void setTitular(long titular) {
    this.titular = titular;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(balance);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((tipoCuenta == null) ? 0 : tipoCuenta.hashCode());
    result = prime * result + ((moneda == null) ? 0 : moneda.hashCode());
    result = prime * result + (int) (titular ^ (titular >>> 32));
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
    CuentaRequestDto other = (CuentaRequestDto) obj;
    if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance)) {
      return false;
    }
    if (tipoCuenta == null) {
      if (other.tipoCuenta != null) {
        return false;
      }
    } else if (!tipoCuenta.equals(other.tipoCuenta)) {
      return false;
    }
    if (moneda == null) {
      if (other.moneda != null) {
        return false;
      }
    } else if (!moneda.equals(other.moneda)) {
      return false;
    }
    if (titular != other.titular) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CuentaRequestDto{balance="
        + balance
        + ", tipoCuenta="
        + tipoCuenta
        + ", moneda="
        + moneda
        + ", titular="
        + titular
        + "}";
  }
}
