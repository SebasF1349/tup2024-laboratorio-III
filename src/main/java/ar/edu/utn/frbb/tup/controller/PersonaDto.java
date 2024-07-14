package ar.edu.utn.frbb.tup.controller;

public class PersonaDto {
  private String nombre;
  private String apellido;
  private String dni;
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

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
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
    result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
    if (dni == null) {
      if (other.dni != null) {
        return false;
      }
    } else if (!dni.equals(other.dni)) {
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
