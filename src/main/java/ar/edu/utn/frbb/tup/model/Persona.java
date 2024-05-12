package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

public class Persona {
  private String dni;
  private String nombre;
  private String apellido;
  private Direccion direccion;
  private String nroTelefono;
  private LocalDate fechaNacimiento;

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

  public boolean setDni(String dni) {
    this.dni = dni;
    return true;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
  }

  public String getNroTelefono() {
    return nroTelefono;
  }

  public void setNroTelefono(String nroTelefono) {
    this.nroTelefono = nroTelefono;
  }
}
