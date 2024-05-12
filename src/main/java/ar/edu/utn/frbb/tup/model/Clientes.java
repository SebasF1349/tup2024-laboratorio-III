package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clientes {
  private static List<Cliente> clientes = new ArrayList<>();

  private Clientes() {}

  private static class ClientesHolder {
    public static final Clientes instance = new Clientes();
  }

  public static Clientes getInstance() {
    return ClientesHolder.instance;
  }

  public List<Cliente> getClientes() {
    return clientes;
  }

  public void addCliente(Cliente cliente) {
    clientes.add(cliente);
  }

  public Cliente getClienteByDni(String dni) {
    for (Cliente cliente : clientes) {
      if (cliente.getDni().equals(dni)) {
        return cliente;
      }
    }
    return null;
  }

  public Cliente getClienteByCuentaId(int cuentaId) {
    for (Cliente cliente : clientes) {
      if (cliente.hasCuenta(cuentaId)) {
        return cliente;
      }
    }
    return null;
  }

  public boolean existsCuentaById(int cuentaId) {
    for (Cliente cliente : clientes) {
      if (cliente.hasCuenta(cuentaId)) {
        return true;
      }
    }
    return false;
  }

  public Cuenta getCuentaById(int cuentaId) {
    for (Cliente cliente : clientes) {
      Cuenta cuenta = cliente.getCuentaById(cuentaId);
      if (Objects.nonNull(cuenta)) {
        return cuenta;
      }
    }
    return null;
  }

  public void deleteCliente(Cliente cliente) {
    clientes.remove(cliente);
  }
}
