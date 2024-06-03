package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Deposito extends Movimiento {
  public Deposito(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public Deposito(double monto, LocalDateTime diaHora, String movimientoId, Cuenta cuenta) {
    super(monto, diaHora, movimientoId, cuenta);
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.DEBITO;
  }
}
