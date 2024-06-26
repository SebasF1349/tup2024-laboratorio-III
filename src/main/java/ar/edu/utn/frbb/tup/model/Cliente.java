package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona {
  private TipoPersona tipoPersona;
  private LocalDate fechaAlta;
  private Set<Cuenta> cuentas = new HashSet<>();
  private boolean activo;

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

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public Set<Cuenta> getCuentas() {
    return cuentas;
  }

  public Set<Cuenta> getCuentasFiltrada(Cuenta cuenta) {
    Set<Cuenta> cuentasFiltrada = new HashSet<>(this.cuentas);
    cuentasFiltrada.remove(cuenta);
    return cuentasFiltrada;
  }

  public void setCuentas(Set<Cuenta> cuentas) {
    this.cuentas = cuentas;
  }

  public void addCuenta(Cuenta cuenta) throws IllegalArgumentException {
    this.cuentas.add(cuenta);
  }

  public boolean hasCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
    for (Cuenta cuenta : cuentas) {
      if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {
        return true;
      }
    }
    return false;
  }

  public Cuenta getCuentaById(String numeroCuenta) {
    for (Cuenta cuenta : cuentas) {
      if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
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
