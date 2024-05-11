package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.Cliente;
import java.util.Objects;

public class ClienteProcessor extends BaseInputProcessor {
  public Cliente getClienteByDni(Banco banco) {
    String dni = this.getStringInput("Ingrese el dni del Cliente:");
    Cliente cliente = banco.getClienteByDni(dni);
    if (Objects.isNull(cliente)) {
      System.out.println("Cliente no encontrado.");
      scanner.nextLine();
      return null;
    }
    return cliente;
  }
}
