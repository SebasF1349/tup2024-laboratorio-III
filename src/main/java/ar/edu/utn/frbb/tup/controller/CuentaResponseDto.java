package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class CuentaResponseDto {
  @PositiveOrZero private double balance;
  @NotNull private String tipoCuenta;
  @NotNull private String moneda;
  @Positive private long titular;
  private boolean activo;

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

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
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
    result = prime * result + (activo ? 1231 : 1237);
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
    CuentaResponseDto other = (CuentaResponseDto) obj;
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
    if (activo != other.activo) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CuentaResponseDto{balance="
        + balance
        + ", tipoCuenta="
        + tipoCuenta
        + ", moneda="
        + moneda
        + ", titular="
        + titular
        + ", activo="
        + activo
        + "}";
  }
}
