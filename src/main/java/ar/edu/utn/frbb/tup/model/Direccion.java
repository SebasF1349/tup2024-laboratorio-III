package ar.edu.utn.frbb.tup.model;

public class Direccion {
  private String calle;
  private String numero;
  private String departamento;
  private String ciudad;

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getDepartamento() {
    return departamento;
  }

  public void setDepartamento(String departamento) {
    this.departamento = departamento;
  }

  public String getCiudad() {
    return ciudad;
  }

  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }

  public Direccion(String calle, String numero, String departamento, String ciudad) {
    this.calle = calle;
    this.numero = numero;
    this.departamento = departamento;
    this.ciudad = ciudad;
  }

  @Override
  public String toString() {
    return "Direccion: " + this.getCalle() + " " + this.getNumero() + ", " + this.getCiudad();
  }
}
