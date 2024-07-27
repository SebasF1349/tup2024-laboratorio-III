package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class CuentaMovimientosResponseDto {
  @NotNull private long numeroCuenta;
  @NotNull private Set<MovimientoResponseDto> movimientos;

  public long getNumeroCuenta() {
    return numeroCuenta;
  }

  public void setNumeroCuenta(long numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

  public Set<MovimientoResponseDto> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(Set<MovimientoResponseDto> transacciones) {
    this.movimientos = transacciones;
  }

  public void addMovimiento(MovimientoResponseDto transaccion) {
    if (this.movimientos == null) {
      this.movimientos = new HashSet<>();
    }
    this.movimientos.add(transaccion);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (numeroCuenta ^ (numeroCuenta >>> 32));
    result = prime * result + ((movimientos == null) ? 0 : movimientos.hashCode());
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
    CuentaMovimientosResponseDto other = (CuentaMovimientosResponseDto) obj;
    if (numeroCuenta != other.numeroCuenta) {
      return false;
    }
    if (movimientos == null) {
      if (other.movimientos != null) {
        return false;
      }
    } else if (!movimientos.equals(other.movimientos)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CuentaMovimientosResponseDto{numeroCuenta="
        + numeroCuenta
        + ", transacciones="
        + movimientos
        + "}";
  }
}
