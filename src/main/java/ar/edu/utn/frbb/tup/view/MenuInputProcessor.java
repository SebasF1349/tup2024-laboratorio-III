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
            ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
            Cliente clienteVersionAntigua = clienteModifyProcessor.getClienteByDni(banco);
            Cliente cliente = clienteModifyProcessor.modifyCliente(clienteVersionAntigua);
            if (Objects.nonNull(cliente)) {
              banco.getClientes().add(cliente);
            }
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
            if (Objects.nonNull(cliente)) {
              CuentaCreateProcessor cuentaCreateProcessor = new CuentaCreateProcessor();
              Cuenta cuenta = cuentaCreateProcessor.createCuenta();
              cliente.addCuenta(cuenta);
            }
            break;
          }
          //            case 2:
          //                createAccount();
          //                break;
          //            case 3:
          //                performTransaction();
          //                break;
        case 10:
          exit = true;
          break;
        default:
          System.out.println("Opción inválida. Por favor seleccione 1-4.");
      }
      clearScreen();
    }
  }
}
