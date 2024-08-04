package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.Positive;

public class RetiroResponseDto extends MovimientoResponseDto {
  @Positive private long cuenta;

  public RetiroResponseDto() {
    super();
    this.setTipoTransaccion("Credito");
  }

  public long getCuenta() {
    return cuenta;
  }

  public void setCuenta(long cuenta) {
    this.cuenta = cuenta;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (int) (cuenta ^ (cuenta >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RetiroResponseDto other = (RetiroResponseDto) obj;
    if (cuenta != other.cuenta) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RetiroResponseDto{cuenta="
        + cuenta
        + ", getFecha()="
        + getFecha()
        + ", getTipoTransaccion()="
        + getTipoTransaccion()
        + ", getDescripcion()="
        + getDescripcion()
        + ", getMonto()="
        + getMonto()
        + "}";
  }
}
