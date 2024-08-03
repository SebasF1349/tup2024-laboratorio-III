package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.TransferenciaRequestDto;
import ar.edu.utn.frbb.tup.controller.TransferenciaResponseDto;
import java.time.LocalDateTime;

public class Transferencia extends Movimiento {
  private Cuenta cuentaDestino;
  private double montoDebitado;

  public Transferencia(double monto, Transferencia transferencia) {
    super(monto, transferencia.getCuenta());
    this.cuentaDestino = transferencia.getCuentaDestino();
  }

  public Transferencia(double monto, Cuenta cuentaDestino, Cuenta cuenta) {
    super(monto, cuenta);
    this.cuentaDestino = cuentaDestino;
  }

  public Transferencia(
      double monto,
      Cuenta cuentaDestino,
      LocalDateTime diaHora,
      long movimientoId,
      Cuenta cuenta,
      String descripcion) {
    super(monto, diaHora, movimientoId, cuenta, descripcion);
    this.cuentaDestino = cuentaDestino;
  }

  public Transferencia(
      TransferenciaRequestDto transferenciaDto, Cuenta cuenta, Cuenta cuentaDestino) {
    super(transferenciaDto.getMonto(), cuenta);
    this.cuentaDestino = cuentaDestino;
  }

  public Transferencia(TransferenciaRequestDto transferenciaDto, Cuenta cuenta) {
    super(transferenciaDto.getMonto(), cuenta);
  }

  public TransferenciaResponseDto toTransferenciaResponseDto() {
    TransferenciaResponseDto transferenciaDto = new TransferenciaResponseDto();
    transferenciaDto.setMovimientoId(this.getMovimientoId());
    transferenciaDto.setDiaHora(this.getDiaHora().toString());
    transferenciaDto.setMonto(this.getMonto());
    transferenciaDto.setMoneda(this.getCuenta().getMoneda().toString());
    transferenciaDto.setCuentaOrigen(this.getCuenta().getNumeroCuenta());
    transferenciaDto.setCuentaDestino(this.getCuentaDestino().getNumeroCuenta());
    transferenciaDto.setMontoDebitado(this.getMontoDebitado());
    transferenciaDto.setDescripcion(this.getDescripcion());
    return transferenciaDto;
  }

  public Cuenta getCuentaDestino() {
    return cuentaDestino;
  }

  public void setCuentaDestino(Cuenta cuentaDestino) {
    this.cuentaDestino = cuentaDestino;
  }

  public double getMontoDebitado() {
    return montoDebitado;
  }

  public void setMontoDebitado(double montoDebitado) {
    this.montoDebitado = montoDebitado;
  }

  public double getNuevoMontoCuentaOrigen() {
    return getCuenta().getBalance() - getMontoDebitado();
  }

  public double getNuevoMontoCuentaDestino() {
    return getCuentaDestino().getBalance() + getMonto();
  }

  @Override
  public String getTipoMovimiento() {
    return "TRANSFERENCIA";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cuentaDestino == null) ? 0 : cuentaDestino.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Transferencia other = (Transferencia) obj;
    if (cuentaDestino == null) {
      if (other.cuentaDestino != null) {
        return false;
      }
    } else if (!cuentaDestino.equals(other.cuentaDestino)) {
      return false;
    }
    return true;
  }
}
