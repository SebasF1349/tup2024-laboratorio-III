package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona {
  private TipoPersona tipoPersona;
  private LocalDate fechaAlta;
  private Set<Cuenta> cuentas = new HashSet<>();

  @Override
  public boolean setDni(String dni) {
    if (!Clientes.getInstance().existsClienteByDni(dni)) {
      return super.setDni(dni);
    }
    return false;
  }

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

  public void addCuenta(Cuenta cuenta) throws Exception {
    if (Clientes.getInstance().existsCuentaById(cuenta.getId())) {
      throw new Exception(
          "No fue posible agregar la Cuenta. La misma ya posee un Cliente asociado.");
    }
    this.cuentas.add(cuenta);
  }

  public boolean hasCuenta(int idCuenta) {
    for (Cuenta cuenta : cuentas) {
      if (cuenta.getId() == idCuenta) {
        return true;
      }
    }
    return false;
  }

  public Cuenta getCuentaById(int idCuenta) {
    for (Cuenta cuenta : cuentas) {
      if (cuenta.getId() == idCuenta) {
        return cuenta;
      }
    }
    return null;
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
