package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.Cliente;
import java.util.Objects;

public class MenuInputProcessor extends BaseInputProcessor {
  ClienteInputProcessor clienteInputProcessor = new ClienteInputProcessor();
  ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
  boolean exit = false;

  public void renderMenu(Banco banco) {

    while (!exit) {
      System.out.println("Bienvenido a la aplicación de Banco!");
      System.out.println("1. Crear un nuevo Cliente");
      System.out.println("2. Modificar un Cliente existente");
      System.out.println("3. Crear una nueva Cuenta");
      System.out.println("4. Generar un movimiento");
      System.out.println("5. Salir");
      System.out.print("Ingrese su opción (1-4): ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline character

      switch (choice) {
        case 1:
          {
            Cliente cliente = clienteInputProcessor.ingresarCliente();
            banco.getClientes().add(cliente);
            break;
          }
        case 2:
          {
            Cliente cliente = clienteModifyProcessor.modifyClienteByDni(banco);
            if (Objects.nonNull(cliente)) {
              banco.getClientes().add(cliente);
            }
            break;
          }
          //            case 2:
          //                createAccount();
          //                break;
          //            case 3:
          //                performTransaction();
          //                break;
        case 4:
          exit = true;
          break;
        default:
          System.out.println("Opción inválida. Por favor seleccione 1-4.");
      }
      clearScreen();
    }
  }
}
