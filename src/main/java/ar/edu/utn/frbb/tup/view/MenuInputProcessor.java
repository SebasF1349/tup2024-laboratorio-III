package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuInputProcessor extends BaseInputProcessor {
  boolean exit = false;

  public void renderMenu(Banco banco) {

    List<String> menuOptions =
        Arrays.asList(
            "Crear un nuevo Cliente",
            "Modificar un Cliente existente",
            "Eliminar Cliente",
            "Crear una nueva Cuenta",
            "Generar un Movimiento");

    while (!exit) {
      System.out.println("Bienvenido a la aplicación de Banco!");
      System.out.println("Elija la operación que desea realizar.");
      for (int i = 0; i < menuOptions.size(); i++) {
        System.out.println((i + 1) + ". " + menuOptions.get(i));
      }
      System.out.println((menuOptions.size() + 1) + ". Salir");
      System.out.print("Ingrese su opción (1-" + (menuOptions.size() + 1) + "): ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline character

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
            ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
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
