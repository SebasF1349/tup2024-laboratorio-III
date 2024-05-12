package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MenuInputProcessor extends BaseInputProcessor {
  private boolean exit = false;

  public void renderMenu(Banco banco) {

    clearScreen();

    List<String> menuOptions =
        Arrays.asList(
            "Crear un nuevo Cliente",
            "Modificar un Cliente existente",
            "Eliminar Cliente",
            "Crear una nueva Cuenta",
            "Realizar una Operación",
            "Salir");

    while (!exit) {
      int choice =
          this.getMultipleOptionsInput("Bienvenido a la aplicación de Banco!", menuOptions);

      switch (choice) {
        case 1:
          {
            ClienteCreateProcessor clienteInputProcessor = new ClienteCreateProcessor();
            Cliente cliente = clienteInputProcessor.ingresarCliente();
            banco.getClientes().add(cliente);
            break;
          }
        case 2:
          {
            ClienteProcessor clienteProcessor = new ClienteProcessor();
            Cliente clienteVersionAntigua = clienteProcessor.getClienteByDni(banco);
            if (Objects.isNull(clienteVersionAntigua)) {
              break;
            }
            ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
            Cliente cliente = clienteModifyProcessor.modifyCliente(clienteVersionAntigua);
            if (Objects.isNull(cliente)) {
              break;
            }
            banco.getClientes().add(cliente);
            break;
          }
        case 3:
          {
            ClienteDeleteProcessor clienteDeleteProcessor = new ClienteDeleteProcessor();
            clienteDeleteProcessor.deleteCliente(banco);
            break;
          }
        case 4:
          {
            ClienteProcessor clienteProcessor = new ClienteProcessor();
            Cliente cliente = clienteProcessor.getClienteByDni(banco);
            if (Objects.isNull(cliente)) {
              break;
            }
            CuentaCreateProcessor cuentaCreateProcessor = new CuentaCreateProcessor();
            Cuenta cuenta = cuentaCreateProcessor.createCuenta();
            cliente.addCuenta(cuenta);
            break;
          }
        case 5:
          {
            ClienteProcessor clienteProcessor = new ClienteProcessor();
            Cliente cliente = clienteProcessor.getClienteByDni(banco);
            if (Objects.isNull(cliente)) {
              break;
            }
            Set<Cuenta> cuentasSet = cliente.getCuentas();
            if (cuentasSet.isEmpty()) {
              System.out.println(
                  "El cliente selecionado no tiene cuentas sobre las cuales operar.");
              scanner.nextLine();
              break;
            }
            List<Cuenta> cuentasList = new ArrayList<>();
            List<String> cuentasStr = new ArrayList<>();
            for (Cuenta cuenta : cuentasSet) {
              cuentasList.add(cuenta);
              cuentasStr.add(cuenta.toString());
            }
            int cuentaChoice =
                this.getMultipleOptionsInput("Elija la cuenta sobre la cual operar:", cuentasStr);
            OperacionMenuProcessor operacionMenuProcessor = new OperacionMenuProcessor();
            operacionMenuProcessor.renderMenu(cuentasList.get(cuentaChoice - 1));
            break;
          }
        case 6:
          exit = true;
          break;
        default:
          System.out.println("Opción no implementada");
      }
      clearScreen();
    }
  }
}
