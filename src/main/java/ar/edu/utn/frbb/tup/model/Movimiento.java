package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public abstract class Movimiento extends Operacion {
  private Cuenta cuenta;
  private long movimientoId;
  private LocalDateTime diaHora;
  private double monto;

  public Movimiento(double monto, Cuenta cuenta) {
    this.diaHora = LocalDateTime.now();
    this.monto = monto;
    this.cuenta = cuenta;
  }

  public Movimiento(double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta) {
    this.movimientoId = movimientoId;
    this.diaHora = diaHora;
    this.monto = monto;
    this.cuenta = cuenta;
  }

  public Cuenta getCuenta() {
    return cuenta;
  }

  public void setCuenta(Cuenta cuenta) {
    this.cuenta = cuenta;
  }

  public Long getMovimientoId() {
    return movimientoId;
  }

  public LocalDateTime getDiaHora() {
    return diaHora;
  }

  public double getMonto() {
    return monto;
  }

  public abstract TipoTransaccion getTipoTransaccion();

  public double actualizarCuentaMonto(double montoCuenta) {
    switch (getTipoTransaccion()) {
      case DEBITO:
        return montoCuenta + this.getMonto();
      case CREDITO:
        return montoCuenta - this.getMonto();
      default:
        throw new IllegalArgumentException("Unhandled TipoTransaccion");
    }
  }

  public abstract String getTipoMovimiento();
}
