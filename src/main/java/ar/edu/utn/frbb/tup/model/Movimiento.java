package ar.edu.utn.frbb.tup.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

public abstract class Movimiento extends Operacion {
  private String movimientoId;
  private LocalDateTime diaHora;
  private double monto;

  public Movimiento(double monto, Cuenta cuenta) {
    this.movimientoId = UUID.randomUUID().toString();
    this.diaHora = LocalDateTime.now();
    this.monto = monto;
  }

  public Movimiento(double monto, LocalDateTime diaHora, long movimientoId) {
    this.movimientoId = movimientoId;
    this.diaHora = diaHora;
    this.monto = monto;
  }

  public String getMovimientoId() {
    return movimientoId;
  }

  public LocalDateTime getDiaHora() {
    return diaHora;
  }

  public double getMonto() {
    return monto;
  }

  public abstract TipoMovimiento getTipoMovimiento();

  public double actualizarCuentaMonto(double montoCuenta) {
    switch (getTipoMovimiento()) {
      case DEBITO:
        return montoCuenta + this.getMonto();
      case CREDITO:
        return montoCuenta - this.getMonto();
      default:
        throw new IllegalArgumentException("Unhandled TipoMovimiento");
    }
  }
}
