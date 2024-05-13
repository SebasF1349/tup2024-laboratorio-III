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
            Cliente clienteVersionAntigua = this.getClienteByDni();
            if (Objects.isNull(clienteVersionAntigua)) {
              break;
            }
            ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
            Cliente cliente = clienteModifyProcessor.modifyCliente(clienteVersionAntigua);
            clientes.getClientes().add(cliente);
            break;
          }
        case 3:
          {
            Cliente cliente = this.getClienteByDni();
            if (Objects.isNull(cliente)) {
              break;
            }
            ClienteDeleteProcessor clienteDeleteProcessor = new ClienteDeleteProcessor();
            clienteDeleteProcessor.deleteCliente(cliente);
            break;
          }
        case 4:
          {
            Cliente cliente = this.getClienteByDni();
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
            Cliente cliente = this.getClienteByDni();
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
