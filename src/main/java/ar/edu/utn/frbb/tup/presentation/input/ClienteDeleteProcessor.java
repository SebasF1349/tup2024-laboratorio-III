package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Clientes;

public class ClienteDeleteProcessor extends ClienteProcessor {
  public void deleteCliente(Cliente cliente) {
    System.out.println(
        "¿Esta seguro que quiere eliminar al cliente "
            + cliente.getApellido()
            + ", "
            + cliente.getNombre()
            + "(DNI: "
            + cliente.getDni()
            + ")? Ingrese S para confirmar");
    String response = scanner.nextLine();
    if (response.equalsIgnoreCase("s")) {
      Clientes.getInstance().deleteCliente(cliente);
      System.out.println("Cliente eliminado.");
    } else {
      System.out.println("El Cliente no ha sido eliminado.");
    }
    scanner.nextLine();
  }
}
