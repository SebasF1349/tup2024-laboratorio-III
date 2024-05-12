package ar.edu.utn.frbb.tup.model;

public class Transferencia extends Movimiento {
  private boolean esCuentaPropia;
  private int idCuentaDestino;
  private boolean esDestinatario;

  public Transferencia(
      double monto, boolean esCuentaPropia, int idCuentaDestino, boolean esDestinatario) {
    super(monto);
    this.esCuentaPropia = esCuentaPropia;
    this.idCuentaDestino = idCuentaDestino;
    this.esDestinatario = esDestinatario;
  }

  public boolean isEsDestinatario() {
    return esDestinatario;
  }

  public void setEsDestinatario(boolean recipiente) {
    this.esDestinatario = recipiente;
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
    if (esDestinatario) {
      return TipoMovimiento.DEBITO;
    } else {
      return TipoMovimiento.CREDITO;
    }
  }
}
