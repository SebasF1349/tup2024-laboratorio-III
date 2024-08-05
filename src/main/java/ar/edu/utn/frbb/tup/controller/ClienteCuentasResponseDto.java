package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

public class ClienteCuentasResponseDto {
  @Schema(
      description = "DNI del Titular",
      example = "12345678",
      requiredMode = RequiredMode.REQUIRED)
  @Positive
  private long dni;

  @NotBlank private Set<CuentaResponseDto> cuentas;

  public long getDni() {
    return dni;
  }

  public void setDni(long dni) {
    this.dni = dni;
  }

  public Set<CuentaResponseDto> getCuentas() {
    return cuentas;
  }

  public void setCuentas(Set<CuentaResponseDto> cuentas) {
    this.cuentas = cuentas;
  }

  public void addCuenta(CuentaResponseDto cuenta) {
    if (this.cuentas == null) {
      this.cuentas = new HashSet<>();
    }
    this.cuentas.add(cuenta);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (dni ^ (dni >>> 32));
    result = prime * result + ((cuentas == null) ? 0 : cuentas.hashCode());
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
    ClienteCuentasResponseDto other = (ClienteCuentasResponseDto) obj;
    if (dni != other.dni) {
      return false;
    }
    if (cuentas == null) {
      if (other.cuentas != null) {
        return false;
      }
    } else if (!cuentas.equals(other.cuentas)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ClienteCuentasResponseDto{dni=" + dni + ", cuentas=" + cuentas + "}";
  }
}
