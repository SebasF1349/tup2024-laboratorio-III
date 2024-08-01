package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotNull;

public class ClienteResponseDto extends PersonaDto {
  @NotNull private String tipoPersona;
  @NotNull private String banco;
  private boolean activo;

  public String getTipoPersona() {
    return tipoPersona;
  }

  public void setTipoPersona(String tipoPersona) {
    this.tipoPersona = tipoPersona;
  }

  public String getBanco() {
    return banco;
  }

  public void setBanco(String banco) {
    this.banco = banco;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean active) {
    this.activo = active;
  }

  @Override
  public String toString() {
    return "ClienteResponseDto{tipoPersona="
        + tipoPersona
        + ", banco="
        + banco
        + ", activo="
        + activo
        + ", getNombre()="
        + getNombre()
        + ", getApellido()="
        + getApellido()
        + ", getDni()="
        + getDni()
        + ", getFechaNacimiento()="
        + getFechaNacimiento()
        + "}";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((tipoPersona == null) ? 0 : tipoPersona.hashCode());
    result = prime * result + ((banco == null) ? 0 : banco.hashCode());
    result = prime * result + (activo ? 1231 : 1237);
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
    ClienteResponseDto other = (ClienteResponseDto) obj;
    if (tipoPersona == null) {
      if (other.tipoPersona != null) {
        return false;
      }
    } else if (!tipoPersona.equals(other.tipoPersona)) {
      return false;
    }
    if (banco == null) {
      if (other.banco != null) {
        return false;
      }
    } else if (!banco.equals(other.banco)) {
      return false;
    }
    if (activo != other.activo) {
      return false;
    }
    return true;
  }
}
