package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Deposito extends MovimientoUnidireccional {
  public Deposito(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public Deposito(double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta) {
    super(monto, diaHora, movimientoId, cuenta);
  }

  @Override
  protected TipoTransaccion getTipoTransaccion() {
    return TipoTransaccion.DEBITO;
  }

  @Override
  public String getTipoMovimiento() {
    return "DEPOSITO";
  }
}
