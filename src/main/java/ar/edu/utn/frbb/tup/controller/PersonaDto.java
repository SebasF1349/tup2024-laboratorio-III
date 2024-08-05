package ar.edu.utn.frbb.tup.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PersonaDto {

  @Schema(
      description = "DNI del Cliente",
      example = "12345678",
      requiredMode = RequiredMode.REQUIRED)
  @Positive(message = "Campo debe ser un número positivo")
  private long dni;

  @Schema(
      description = "Nombre del Cliente",
      example = "Federico",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "Campo no puede estar vacio")
  private String nombre;

  @Schema(
      description = "Apellido del Cliente",
      example = "Garcia",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "Campo no puede estar vacio")
  private String apellido;

  @Schema(
      description = "Fecha de Nacimiento del Cliente",
      example = "1998-01-05",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "Campo no puede estar vacio")
  private String fechaNacimiento;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public long getDni() {
    return dni;
  }

  public void setDni(long dni) {
    this.dni = dni;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
    result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
    result = prime * result + (int) (dni ^ (dni >>> 32));
    result = prime * result + ((fechaNacimiento == null) ? 0 : fechaNacimiento.hashCode());
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
    PersonaDto other = (PersonaDto) obj;
    if (nombre == null) {
      if (other.nombre != null) {
        return false;
      }
    } else if (!nombre.equals(other.nombre)) {
      return false;
    }
    if (apellido == null) {
      if (other.apellido != null) {
        return false;
      }
    } else if (!apellido.equals(other.apellido)) {
      return false;
    }
    if (dni != other.dni) {
      return false;
    }
    if (fechaNacimiento == null) {
      if (other.fechaNacimiento != null) {
        return false;
      }
    } else if (!fechaNacimiento.equals(other.fechaNacimiento)) {
      return false;
    }
    return true;
  }
}
