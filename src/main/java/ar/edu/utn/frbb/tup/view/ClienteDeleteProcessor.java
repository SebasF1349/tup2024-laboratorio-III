package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Clientes;
import java.util.Objects;

public class ClienteDeleteProcessor extends ClienteProcessor {
  public void deleteCliente() {
    Clientes clientes = Clientes.getInstance();
    String dni = this.getStringInput("Ingrese el dni del Cliente a eliminar:");
    Cliente cliente = clientes.getClienteByDni(dni);
    if (Objects.isNull(cliente)) {
      System.out.println("Cliente no encontrado.");
    } else {
      System.out.println(
          "¿Esta seguro que quiere eliminar al cliente "
              + cliente.getApellido()
              + ", "
              + cliente.getNombre()
              + "? Ingrese S para confirmar");
      String response = scanner.nextLine();
      if (response.equalsIgnoreCase("s")) {
        clientes.deleteCliente(cliente);
        System.out.println("Cliente eliminado.");
      } else {
        System.out.println("El Cliente no ha sido eliminado.");
      }
    }
    scanner.nextLine();
  }
}
