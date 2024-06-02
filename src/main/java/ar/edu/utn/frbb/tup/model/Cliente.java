package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona {
  private TipoPersona tipoPersona;
  private LocalDate fechaAlta;
  private Set<Cuenta> cuentas = new HashSet<>();

  @Override
  public boolean setDni(long dni) {
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

  public void setCuentas(Set<Cuenta> cuentas) {
    this.cuentas = cuentas;
  }

  public void addCuenta(Cuenta cuenta) throws IllegalArgumentException {
    if (Clientes.getInstance().existsCuentaById(cuenta.getNumeroCuenta())) {
      throw new IllegalArgumentException(
          "No fue posible agregar la Cuenta. La misma ya posee un Cliente asociado.");
    }
    this.cuentas.add(cuenta);
  }

  public boolean hasCuenta(TipoCuenta tipoCuenta, MonedaCuenta moneda) {
    for (Cuenta cuenta : cuentas) {
      if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {
        return true;
      }
    }
    return false;
  }

  public Cuenta getCuentaById(long numeroCuenta) {
    for (Cuenta cuenta : cuentas) {
      if (cuenta.getNumeroCuenta() == numeroCuenta) {
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
