package ar.edu.utn.frbb.tup.model;

public class Direccion {
  private String calle;
  private String numero;
  private String departamento;

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

  public Direccion(String calle, String numero, String departamento) {
    this.calle = calle;
    this.numero = numero;
    this.departamento = departamento;
  }
}
