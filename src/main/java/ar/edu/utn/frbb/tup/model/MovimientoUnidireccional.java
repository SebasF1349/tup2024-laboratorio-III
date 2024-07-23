package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public abstract class MovimientoUnidireccional extends Movimiento {

  public MovimientoUnidireccional(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public MovimientoUnidireccional(
      double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta) {
    super(monto, diaHora, movimientoId, cuenta);
  }

  protected abstract TipoTransaccion getTipoTransaccion();

  public double actualizarCuentaMonto(Cuenta cuenta) {
    switch (getTipoTransaccion()) {
      case DEBITO:
        return cuenta.getBalance() + this.getMonto();
      case CREDITO:
        return cuenta.getBalance() - this.getMonto();
      default:
        // TODO: find better way to handle impossible cases
        throw new IllegalArgumentException("Unhandled TipoTransaccion");
    }
  }
}
