package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import org.springframework.stereotype.Component;

@Component
public class ClienteDeleteProcessor extends ClienteProcessor {
  public void deleteCliente() {
    Cliente cliente;
    try {
      cliente = this.getClienteByDni();
    } catch (ClienteNoExistsException e) {
      System.out.println(e.getMessage());
      return;
    }

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
      try {
        clienteService.eliminarCliente(cliente);
      } catch (ClienteNoExistsException e) {
        e.printStackTrace();
      }
      System.out.println("Cliente eliminado.");
    } else {
      System.out.println("El Cliente no ha sido eliminado.");
    }
    scanner.nextLine();
  }
}
