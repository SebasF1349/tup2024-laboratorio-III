package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Clientes;
import ar.edu.utn.frbb.tup.model.Cuenta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ClienteProcessor extends BaseInputProcessor {
  public Cliente getClienteByDni() {
    String dni = this.getStringInput("Ingrese el dni del Cliente:");
    Cliente cliente = Clientes.getInstance().getClienteByDni(dni);
    if (Objects.isNull(cliente)) {
      System.out.println("Cliente no encontrado.");
      scanner.nextLine();
      return null;
    }
    return cliente;
  }

  public Cuenta getCuentaId(Set<Cuenta> cuentas) {
    if (cuentas.isEmpty()) {
      System.out.println("El cliente selecionado no tiene cuentas sobre las cuales operar.");
      scanner.nextLine();
      return null;
    }
    List<Cuenta> cuentasList = new ArrayList<>();
    List<String> cuentasStr = new ArrayList<>();
    for (Cuenta cuenta : cuentas) {
      cuentasList.add(cuenta);
      cuentasStr.add(cuenta.toString());
    }
    int cuentaChoice =
        this.getMultipleOptionsInput("Elija la cuenta sobre la cual operar:", cuentasStr);
    return cuentasList.get(cuentaChoice - 1);
  }
}
