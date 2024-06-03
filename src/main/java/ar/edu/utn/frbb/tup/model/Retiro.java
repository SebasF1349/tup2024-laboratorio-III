package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Retiro extends Movimiento {
  public Retiro(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public Retiro(double monto, LocalDateTime diaHora, String movimientoId, Cuenta cuenta) {
    super(monto, diaHora, movimientoId, cuenta);
  }

  public TipoTransaccion getTipoTransaccion() {
    return TipoTransaccion.CREDITO;
  }

  @Override
  public String getTipoMovimiento() {
    return "RETIRO";
  }
}
