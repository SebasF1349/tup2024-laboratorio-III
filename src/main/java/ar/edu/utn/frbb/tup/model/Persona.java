package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.time.Period;

public class Persona {
  private String dni;
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;

  public Persona() {}

  public Persona(String dni, String apellido, String nombre, String fechaNacimiento) {
    this.dni = dni;
    this.apellido = apellido;
    this.nombre = nombre;
    this.fechaNacimiento = LocalDate.parse(fechaNacimiento);
  }

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

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public int getEdad() {
    LocalDate currentDate = LocalDate.now();
    Period agePeriod = Period.between(fechaNacimiento, currentDate);
    return agePeriod.getYears();
  }
}
