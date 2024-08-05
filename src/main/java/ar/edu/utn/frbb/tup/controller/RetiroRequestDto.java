package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Positive;

public class RetiroRequestDto extends MovimientoRequestDto {
  @Schema(
      description = "Id de la cuenta a la que se le realiza el retiro",
      example = "123456",
      requiredMode = RequiredMode.REQUIRED)
  @Positive(message = "Campo debe ser un numero positivo")
  private long cuenta;

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
    RetiroRequestDto other = (RetiroRequestDto) obj;
    if (cuenta != other.cuenta) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RetiroRequestDto{cuenta="
        + cuenta
        + ", getMonto()="
        + getMonto()
        + ", getMoneda()="
        + getMoneda()
        + "}";
  }
}
