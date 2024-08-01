package ar.edu.utn.frbb.tup.controller;

import jakarta.validation.constraints.NotNull;

public class ClienteRequestDto extends PersonaDto {
  @NotNull private String tipoPersona;
  @NotNull private String banco;

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tipoPersona == null) ? 0 : tipoPersona.hashCode());
    result = prime * result + ((banco == null) ? 0 : banco.hashCode());
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
    ClienteRequestDto other = (ClienteRequestDto) obj;
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
    return true;
  }

  @Override
  public String toString() {
    return "ClienteDto{tipoPersona="
        + tipoPersona
        + ", banco="
        + banco
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
}
