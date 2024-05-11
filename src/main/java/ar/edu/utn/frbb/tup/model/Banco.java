package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;

public class Banco {
  private List<Cliente> clientes = new ArrayList<>();

  public List<Cliente> getClientes() {
    return clientes;
  }

  public void setClientes(List<Cliente> clientes) {
    this.clientes = clientes;
  }

  public Cliente getClienteByDni(String dni) {
    for (Cliente cliente : clientes) {
      if (cliente.getDni().equals(dni)) {
        return cliente;
      }
    }
    return null;
  }

  public void deleteCliente(Cliente cliente) {
    clientes.remove(cliente);
  }
}
