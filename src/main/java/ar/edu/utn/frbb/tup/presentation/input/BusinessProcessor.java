package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BusinessProcessor extends BaseInputProcessor {
  public Cliente getClienteByDni() throws ClienteNoExistsException {
    String dni = this.getStringInput("Ingrese el dni del Cliente:");
    Cliente cliente = clienteService.buscarClientePorDni(dni);
    return cliente;
  }

  public Cuenta getCuentaId(Set<Cuenta> cuentas) {
    if (cuentas.isEmpty()) {
      System.out.println("El cliente selecionado no tiene cuentas sobre las cuales operar.");
      scanner.nextLine();
      return null;
    }
    List<Cuenta> cuentasList = new ArrayList<>();
    cuentasList.addAll(cuentas);
    int cuentaChoice =
        this.getMultipleOptionsInput("Elija la cuenta sobre la cual operar:", cuentasList);
    return cuentasList.get(cuentaChoice - 1);
  }
}
