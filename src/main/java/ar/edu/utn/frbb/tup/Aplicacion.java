package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Clientes;
import ar.edu.utn.frbb.tup.model.Direccion;
import ar.edu.utn.frbb.tup.view.MenuInputProcessor;

class Aplicacion {

  public static void main(String args[]) {
    Cliente cliente = new Cliente();
    cliente.setDni("123");
    cliente.setNombre("Y");
    cliente.setApellido("A");
    Direccion direccion = new Direccion("e", "33", "", "BB");
    cliente.setDireccion(direccion);
    Clientes.getInstance().getClientes().add(cliente);

    Cliente cliente2 = new Cliente();
    cliente2.setDni("123");
    cliente2.setNombre("Y");
    cliente2.setApellido("A");
    cliente2.setDireccion(direccion);
    Clientes.getInstance().getClientes().add(cliente2);

    MenuInputProcessor menuInputProcessor = new MenuInputProcessor();
    menuInputProcessor.renderMenu();
  }
}
