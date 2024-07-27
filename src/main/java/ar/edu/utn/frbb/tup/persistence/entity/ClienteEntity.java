package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteEntity extends BaseEntity {
  private final String tipoPersona;
  private final String nombre;
  private final String apellido;
  private final LocalDate fechaAlta;
  private final LocalDate fechaNacimiento;
  private final boolean activo;
  private final String banco;
  private List<Long> cuentas;

  public ClienteEntity(Cliente cliente) {
    super(cliente.getDni());
    this.tipoPersona =
        cliente.getTipoPersona() != null ? cliente.getTipoPersona().getDescripcion() : null;
    this.nombre = cliente.getNombre();
    this.apellido = cliente.getApellido();
    this.fechaAlta = cliente.getFechaAlta();
    this.fechaNacimiento = cliente.getFechaNacimiento();
    this.activo = cliente.isActivo();
    this.banco = cliente.getBanco();
    this.cuentas = new ArrayList<>();
    if (cliente.getCuentas() != null && !cliente.getCuentas().isEmpty()) {
      for (Cuenta c : cliente.getCuentas()) {
        cuentas.add(c.getNumeroCuenta());
      }
    }
  }

  public void addCuenta(Cuenta cuenta) {
    if (cuentas == null) {
      cuentas = new ArrayList<>();
    }
    cuentas.add(cuenta.getNumeroCuenta());
  }

  public Cliente toCliente() {
    Cliente cliente = new Cliente();
    cliente.setDni(this.getId());
    cliente.setNombre(this.nombre);
    cliente.setApellido(this.apellido);
    cliente.setTipoPersona(TipoPersona.fromString(this.tipoPersona));
    cliente.setFechaAlta(this.fechaAlta);
    cliente.setFechaNacimiento(this.fechaNacimiento);
    cliente.setActivo(this.activo);
    cliente.setBanco(this.banco);

    return cliente;
  }

  public String getTipoPersona() {
    return tipoPersona;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public LocalDate getFechaAlta() {
    return fechaAlta;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public List<Long> getCuentas() {
    return cuentas;
  }

  public void setCuentas(List<Long> cuentas) {
    this.cuentas = cuentas;
  }

  @Override
  public String toString() {
    return "ClienteEntity{tipoPersona="
        + tipoPersona
        + ", nombre="
        + nombre
        + ", apellido="
        + apellido
        + ", fechaAlta="
        + fechaAlta
        + ", fechaNacimiento="
        + fechaNacimiento
        + ", activo="
        + activo
        + ", cuentas="
        + cuentas
        + "}";
  }
}
