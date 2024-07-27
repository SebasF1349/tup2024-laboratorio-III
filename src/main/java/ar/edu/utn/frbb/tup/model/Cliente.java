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
  private String banco;

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
    activo = true;
    banco = clienteDto.getBanco();
  }

  public ClienteDto toClienteDto() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setDni(this.getDni());
    clienteDto.setNombre(this.getNombre());
    clienteDto.setApellido(this.getApellido());
    clienteDto.setFechaNacimiento(this.getFechaNacimiento().toString());
    clienteDto.setTipoPersona(this.getTipoPersona().toString());
    clienteDto.setBanco(this.getBanco());
    return clienteDto;
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

  public Cuenta getCuenta(long numeroCuenta) {
    for (Cuenta cuenta : cuentas) {
      if (cuenta.getNumeroCuenta() == numeroCuenta) {
        return cuenta;
      }
    }
    return null;
  }

  public void setCuentas(Set<Cuenta> cuentas) {
    this.cuentas = cuentas;
  }

  public void addCuenta(Cuenta cuenta) throws IllegalArgumentException {
    this.cuentas.add(cuenta);
  }

  public boolean hasCuentaSameTipo(TipoCuenta tipoCuenta, TipoMoneda moneda) {
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

  public void deleteCuenta(Cuenta cuenta) {
    cuentas.remove(cuenta);
  }

  public String getBanco() {
    return banco;
  }

  public void setBanco(String banco) {
    this.banco = banco;
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

  @Override
  public String toString() {
    return "Cliente{tipoPersona="
        + tipoPersona
        + ", fechaAlta="
        + fechaAlta
        + ", cuentas="
        + cuentas
        + ", activo="
        + activo
        + ", banco="
        + banco
        + "}";
  }
}
