package ar.edu.utn.frbb.tup.model;

public class Transferencia extends Movimiento {
  private boolean esCuentaPropia;
  private int idCuentaDestino;

  public Transferencia(double monto, boolean esCuentaPropia, int idCuentaDestino) {
    super(monto);
    this.esCuentaPropia = esCuentaPropia;
    this.idCuentaDestino = idCuentaDestino;
  }

  public boolean isEsCuentaPropia() {
    return esCuentaPropia;
  }

  public void setEsCuentaPropia(boolean esCuentaPropia) {
    this.esCuentaPropia = esCuentaPropia;
  }

  public int getIdCuentaDestino() {
    return idCuentaDestino;
  }

  public void setIdCuentaDestino(int idCuentaDestino) {
    this.idCuentaDestino = idCuentaDestino;
  }

  public TipoMovimiento getTipoMovimiento() {
    return TipoMovimiento.CREDITO;
  }
}
