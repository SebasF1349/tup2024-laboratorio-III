package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona {
  private TipoPersona tipoPersona;
  private LocalDate fechaAlta;
  private Set<Cuenta> cuentas = new HashSet<>();
  private boolean activo;

  public Cliente() {
    super();
  }

  public Cliente(ClienteDto clienteDto) {
    super(
        clienteDto.getDni(),
        clienteDto.getApellido(),
        clienteDto.getNombre(),
        clienteDto.getFechaNacimiento());
    fechaAlta = LocalDate.now();
    tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tipoPersona == null) ? 0 : tipoPersona.hashCode());
    result = prime * result + ((fechaAlta == null) ? 0 : fechaAlta.hashCode());
    result = prime * result + ((cuentas == null) ? 0 : cuentas.hashCode());
    result = prime * result + (activo ? 1231 : 1237);
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
    Cliente other = (Cliente) obj;
    if (tipoPersona != other.tipoPersona) {
      return false;
    }
    if (fechaAlta == null) {
      if (other.fechaAlta != null) {
        return false;
      }
    } else if (!fechaAlta.equals(other.fechaAlta)) {
      return false;
    }
    if (cuentas == null) {
      if (other.cuentas != null) {
        return false;
      }
    } else if (!cuentas.equals(other.cuentas)) {
      return false;
    }
    if (activo != other.activo) {
      return false;
    }
    return true;
  }
}
