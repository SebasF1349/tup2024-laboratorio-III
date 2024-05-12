package ar.edu.utn.frbb.tup.model;

public class Deposito extends Movimiento {
  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.DEBITO;
  }
}
