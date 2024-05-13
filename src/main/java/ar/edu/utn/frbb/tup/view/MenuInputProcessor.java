package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Clientes;
import ar.edu.utn.frbb.tup.model.Cuenta;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuInputProcessor extends ClienteProcessor {
  private boolean exit = false;

  public void renderMenu() {
    Clientes clientes = Clientes.getInstance();

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
            if (Objects.isNull(cliente)) {
              break;
            }
            clientes.getClientes().add(cliente);
            break;
          }
        case 2:
          {
            ClienteProcessor clienteProcessor = new ClienteProcessor();
            Cliente clienteVersionAntigua = clienteProcessor.getClienteByDni();
            if (Objects.isNull(clienteVersionAntigua)) {
              break;
            }
            ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
            Cliente cliente = clienteModifyProcessor.modifyCliente(clienteVersionAntigua);
            if (Objects.isNull(cliente)) {
              break;
            }
            clientes.getClientes().add(cliente);
            break;
          }
        case 3:
          {
            ClienteDeleteProcessor clienteDeleteProcessor = new ClienteDeleteProcessor();
            clienteDeleteProcessor.deleteCliente();
            break;
          }
        case 4:
          {
            ClienteProcessor clienteProcessor = new ClienteProcessor();
            Cliente cliente = clienteProcessor.getClienteByDni();
            if (Objects.isNull(cliente)) {
              break;
            }
            CuentaCreateProcessor cuentaCreateProcessor = new CuentaCreateProcessor();
            Cuenta cuenta = cuentaCreateProcessor.createCuenta();
            try {
              cliente.addCuenta(cuenta);
            } catch (Exception e) {
              System.out.println(e.getMessage());
            }
            break;
          }
        case 5:
          {
            ClienteProcessor clienteProcessor = new ClienteProcessor();
            Cliente cliente = clienteProcessor.getClienteByDni();
            if (Objects.isNull(cliente)) {
              break;
            }
            Cuenta cuenta = this.getCuentaId(cliente.getCuentas());
            if (Objects.isNull(cuenta)) {
              break;
            }
            OperacionMenuProcessor operacionMenuProcessor = new OperacionMenuProcessor();
            operacionMenuProcessor.renderMenu(cuenta);
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
