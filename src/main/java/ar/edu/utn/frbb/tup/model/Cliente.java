package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona {
  private TipoPersona tipoPersona;
  private LocalDate fechaAlta;
  private Set<Cuenta> cuentas = new HashSet<>();

  public TipoPersona getTipoPersona() {
    return tipoPersona;
  }

  public void setTipoPersona(TipoPersona tipoPersona) {
    this.tipoPersona = tipoPersona;
  }

  public LocalDate getFechaAlta() {
    return fechaAlta;
  }

  public void setFechaAlta(LocalDate fechaAlta) {
    this.fechaAlta = fechaAlta;
  }

  public Set<Cuenta> getCuentas() {
    return cuentas;
  }

  public void addCuenta(Cuenta cuenta) {
    this.cuentas.add(cuenta);
  }

  @Override
  public String toString() {
    return "---------------------------------------"
        + "\nCliente\nApellido y nombre: "
        + this.getApellido()
        + ", "
        + this.getNombre()
        + "\nDNI: "
        + this.getDni()
        + "\nFecha de Nacimiento: "
        + this.getFechaNacimiento()
        + "\n"
        + this.getDireccion()
        + "\nTeléfono: "
        + this.getNroTelefono()
        + "\nTipo de Persona: "
        + this.tipoPersona
        + "\nFecha de Alta: "
        + this.fechaAlta
        + "\nCuentas: "
        + this.cuentas;
  }
}
