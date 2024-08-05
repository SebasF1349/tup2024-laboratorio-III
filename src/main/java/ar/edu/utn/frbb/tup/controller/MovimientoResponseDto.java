package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class MovimientoResponseDto {
  @Schema(description = "Id del Movimiento", example = "123", requiredMode = RequiredMode.REQUIRED)
  @Positive
  private long movimientoId;

  @Schema(
      description = "Fecha de realización del movimiento",
      example = "2024-01-05",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String fecha;

  @Schema(
      description = "Tipo de la Transaccion",
      example = "Credito",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String tipoTransaccion;

  @Schema(
      description = "Descripcion del movimiento",
      example = "Pago en el supermercado",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String descripcion;

  @Schema(
      description = "Monto del movimiento",
      example = "1000",
      requiredMode = RequiredMode.REQUIRED)
  @Positive
  private double monto;

  @Schema(description = "Monto debitado", example = "1000", requiredMode = RequiredMode.REQUIRED)
  @Positive
  private double montoDebitado;

  @Schema(
      description = "Moneda del movimiento",
      example = "pesos",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String moneda;

  public long getMovimientoId() {
    return movimientoId;
  }

  public void setMovimientoId(long movimientoId) {
    this.movimientoId = movimientoId;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getTipoTransaccion() {
    return tipoTransaccion;
  }

  public void setTipoTransaccion(String tipoTransaccion) {
    this.tipoTransaccion = tipoTransaccion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public double getMontoDebitado() {
    return montoDebitado;
  }

  public void setMontoDebitado(double montoDebitado) {
    this.montoDebitado = montoDebitado;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
    result = prime * result + ((tipoTransaccion == null) ? 0 : tipoTransaccion.hashCode());
    result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
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
    MovimientoResponseDto other = (MovimientoResponseDto) obj;
    if (fecha == null) {
      if (other.fecha != null) {
        return false;
      }
    } else if (!fecha.equals(other.fecha)) {
      return false;
    }
    if (tipoTransaccion == null) {
      if (other.tipoTransaccion != null) {
        return false;
      }
    } else if (!tipoTransaccion.equals(other.tipoTransaccion)) {
      return false;
    }
    if (descripcion == null) {
      if (other.descripcion != null) {
        return false;
      }
    } else if (!descripcion.equals(other.descripcion)) {
      return false;
    }
    if (Double.doubleToLongBits(monto) != Double.doubleToLongBits(other.monto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MovimientoResponseDto{fecha="
        + fecha
        + ", tipoTransaccion="
        + tipoTransaccion
        + ", descripcion="
        + descripcion
        + ", monto="
        + monto
        + "}";
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }
}
