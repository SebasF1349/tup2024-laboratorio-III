package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import java.time.LocalDateTime;

public abstract class MovimientoUnidireccional extends Movimiento {

  public MovimientoUnidireccional(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public MovimientoUnidireccional(
      double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta, String descripcion) {
    super(monto, diaHora, movimientoId, cuenta, descripcion);
  }

  protected abstract TipoTransaccion getTipoTransaccion();

  @Override
  public String getTipoMovimiento() {
    return this.getTipoTransaccion().toString();
  }

  public double actualizarCuentaMonto(Cuenta cuenta) throws ImpossibleException {
    switch (getTipoTransaccion()) {
      case DEBITO:
        return cuenta.getBalance() + this.getMonto();
      case CREDITO:
        return cuenta.getBalance() - this.getMonto();
      default:
        throw new ImpossibleException();
    }
  }
}
