package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Retiro extends Movimiento {
  public Retiro(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public Retiro(double monto, LocalDateTime diaHora, String movimientoId, Cuenta cuenta) {
    super(monto, diaHora, movimientoId, cuenta);
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.CREDITO;
  }
}
