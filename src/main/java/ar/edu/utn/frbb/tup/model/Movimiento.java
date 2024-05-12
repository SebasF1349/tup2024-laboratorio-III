package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public abstract class Movimiento extends Operacion {
  private int id;
  private LocalDateTime diaHora;
  private double monto;

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setDiaHora(LocalDateTime diaHora) {
    this.diaHora = diaHora;
  }

  public LocalDateTime getDiaHora() {
    return diaHora;
  }

  public void setMonto(double monto) {
    this.monto = monto;
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
