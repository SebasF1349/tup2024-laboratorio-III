package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Retiro extends Movimiento {
  public Retiro(double monto) {
    super(monto);
  }

  public Retiro(double monto, LocalDateTime diaHora, long movimientoId) {
    super(monto, diaHora, movimientoId);
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.CREDITO;
  }
}
