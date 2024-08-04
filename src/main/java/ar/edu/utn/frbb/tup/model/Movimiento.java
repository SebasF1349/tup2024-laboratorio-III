package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.MovimientoResponseDto;
import java.time.LocalDateTime;

public abstract class Movimiento {
  private Cuenta cuenta;
  private long movimientoId;
  private LocalDateTime diaHora;
  private double monto;
  private String descripcion;

  public Movimiento(double monto, Cuenta cuenta) {
    this.diaHora = LocalDateTime.now();
    this.monto = monto;
    this.cuenta = cuenta;
    this.descripcion = this.getClass().getSimpleName();
  }

  public Movimiento(
      double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta, String descripcion) {
    this.movimientoId = movimientoId;
    this.diaHora = diaHora;
    this.monto = monto;
    this.cuenta = cuenta;
    this.descripcion = descripcion;
  }

  public MovimientoResponseDto toMovimientoResponseDto(long numeroCuenta) {
    MovimientoResponseDto movimientoResponseDto = new MovimientoResponseDto();
    movimientoResponseDto.setMovimientoId(this.getMovimientoId());
    movimientoResponseDto.setDescripcion(this.getDescripcion());
    movimientoResponseDto.setFecha(this.getDiaHora().toString());
    movimientoResponseDto.setMoneda(this.getCuenta().getMoneda().toString());
    movimientoResponseDto.setMonto(this.getMonto());
    if (this.getTipoMovimiento() == "Transferencia") {
      Transferencia transferencia = (Transferencia) this;
      if (transferencia.getCuenta().getNumeroCuenta() == numeroCuenta) {
        movimientoResponseDto.setTipoTransaccion(TipoTransaccion.CREDITO.toString());
        movimientoResponseDto.setMontoDebitado(transferencia.getMontoDebitado());
      } else if (transferencia.getCuentaDestino().getNumeroCuenta() == numeroCuenta) {
        movimientoResponseDto.setTipoTransaccion(TipoTransaccion.DEBITO.toString());
        movimientoResponseDto.setMontoDebitado(transferencia.getMonto());
      }
    } else {
      movimientoResponseDto.setTipoTransaccion(this.getTipoMovimiento());
      movimientoResponseDto.setMontoDebitado(this.getMonto());
    }
    movimientoResponseDto.setMontoDebitado(this.getMonto());
    return movimientoResponseDto;
  }

  public Cuenta getCuenta() {
    return cuenta;
  }

  public void setCuenta(Cuenta cuenta) {
    this.cuenta = cuenta;
  }

  public Long getMovimientoId() {
    return movimientoId;
  }

  public void setMovimientoId(long movimientoId) {
    this.movimientoId = movimientoId;
  }

  public LocalDateTime getDiaHora() {
    return diaHora;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public abstract String getTipoMovimiento();

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cuenta == null) ? 0 : cuenta.hashCode());
    result = prime * result + (int) (movimientoId ^ (movimientoId >>> 32));
    result = prime * result + ((diaHora == null) ? 0 : diaHora.hashCode());
    long temp;
    temp = Double.doubleToLongBits(monto);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    Movimiento other = (Movimiento) obj;
    if (cuenta == null) {
      if (other.cuenta != null) {
        return false;
      }
    } else if (!cuenta.equals(other.cuenta)) {
      return false;
    }
    if (movimientoId != other.movimientoId) {
      return false;
    }
    if (diaHora == null) {
      if (other.diaHora != null) {
        return false;
      }
    } else if (!diaHora.equals(other.diaHora)) {
      return false;
    }
    if (Double.doubleToLongBits(monto) != Double.doubleToLongBits(other.monto)) {
      return false;
    }
    return true;
  }
}
