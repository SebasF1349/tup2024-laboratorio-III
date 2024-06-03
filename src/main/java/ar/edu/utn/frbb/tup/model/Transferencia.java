package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Transferencia extends Movimiento {
  private boolean esCuentaPropia;
  private String numeroCuentaDestino;
  private boolean esDestinatario;

  public Transferencia(
      double monto, boolean esCuentaPropia, long numeroCuentaDestino, boolean esDestinatario) {
    super(monto);
    this.esCuentaPropia = esCuentaPropia;
    this.numeroCuentaDestino = numeroCuentaDestino;
    this.esDestinatario = esDestinatario;
  }

  public Transferencia(
      double monto,
      boolean esCuentaPropia,
      String numeroCuentaDestino,
      boolean esDestinatario,
      LocalDateTime diaHora,
    super(monto, diaHora, movimientoId);
      String movimientoId,
    this.esCuentaPropia = esCuentaPropia;
    this.numeroCuentaDestino = numeroCuentaDestino;
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

  public String getNumeroCuentaDestino() {
    return numeroCuentaDestino;
  }

  public void setIdCuentaDestino(String numeroCuentaDestino) {
    this.numeroCuentaDestino = numeroCuentaDestino;
  }

  public TipoMovimiento getTipoMovimiento() {
    if (esDestinatario) {
      return TipoMovimiento.DEBITO;
    } else {
      return TipoMovimiento.CREDITO;
    }
  }
}
