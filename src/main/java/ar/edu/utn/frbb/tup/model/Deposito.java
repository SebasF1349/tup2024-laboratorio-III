package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Deposito extends Movimiento {
  public Deposito(double monto) {
    super(monto);
  }

  public Deposito(double monto, LocalDateTime diaHora, long movimientoId) {
    super(monto, diaHora, movimientoId);
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.DEBITO;
  }
}
