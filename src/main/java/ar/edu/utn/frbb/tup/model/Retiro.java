package ar.edu.utn.frbb.tup.model;

public class Retiro extends Movimiento {
  public Retiro(double monto) {
    super(monto);
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.CREDITO;
  }
}
