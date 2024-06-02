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

  public Cliente getClienteByDni(long dni) {
    for (Cliente cliente : clientes) {
      if (cliente.getDni() == dni) {
        return cliente;
      }
    }
    return null;
  }

  public boolean existsClienteByDni(long dni) {
    for (Cliente cliente : clientes) {
      if (cliente.getDni() == dni) {
        return true;
      }
    }
    return false;
  }

  public Cliente getClienteByCuentaId(long numeroCuenta) {
    for (Cliente cliente : clientes) {
      if (cliente.hasCuenta(numeroCuenta)) {
        return cliente;
      }
    }
    return null;
  }

  public boolean existsCuentaById(long numeroCuenta) {
    for (Cliente cliente : clientes) {
      if (cliente.hasCuenta(numeroCuenta)) {
        return true;
      }
    }
    return false;
  }

  public Cuenta getCuentaById(long numeroCuenta) {
    for (Cliente cliente : clientes) {
      Cuenta cuenta = cliente.getCuentaById(numeroCuenta);
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
