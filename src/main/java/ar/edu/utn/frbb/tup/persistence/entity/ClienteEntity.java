package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClienteEntity extends BaseEntity {

  private final String tipoPersona;
  private final String nombre;
  private final String apellido;
  private final LocalDate fechaAlta;
  private final LocalDate fechaNacimiento;
  private List<Long> cuentas;

  public ClienteEntity(Cliente cliente) {
    super(cliente.getDni());
    this.tipoPersona =
        cliente.getTipoPersona() != null ? cliente.getTipoPersona().getDescripcion() : null;
    this.nombre = cliente.getNombre();
    this.apellido = cliente.getApellido();
    this.fechaAlta = cliente.getFechaAlta();
    this.fechaNacimiento = cliente.getFechaNacimiento();
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

    if (!this.cuentas.isEmpty()) {
      Set<Cuenta> c = new HashSet<>();
      CuentaDao cuentaDao = new CuentaDao();
      for (long numeroCuenta : cuentas) {
        Cuenta titular = cuentaDao.find(numeroCuenta);
        c.add(titular);
      }
      cliente.setCuentas(c);
    }

    return cliente;
  }
}
