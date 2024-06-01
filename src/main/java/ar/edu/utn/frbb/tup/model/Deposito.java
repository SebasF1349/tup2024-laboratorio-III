package ar.edu.utn.frbb.tup.model;

public class Deposito extends Movimiento {
  public Deposito(double monto) {
    super(monto);
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.DEBITO;
  }
}
