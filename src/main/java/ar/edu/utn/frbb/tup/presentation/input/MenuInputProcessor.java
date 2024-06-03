package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class MenuInputProcessor extends BusinessProcessor {
  ClienteCreateProcessor clienteCreateProcessor;
  ClienteModifyProcessor clienteModifyProcessor;
  ClienteDeleteProcessor clienteDeleteProcessor;
  CuentaCreateProcessor cuentaCreateProcessor;
  OperacionMenuProcessor operacionMenuProcessor;

  boolean exit = false;

  public MenuInputProcessor(
      ClienteCreateProcessor clienteInputProcessor,
      ClienteModifyProcessor clienteModifyProcessor,
      ClienteDeleteProcessor clienteDeleteProcessor,
      CuentaCreateProcessor cuentaInputProcessor,
      OperacionMenuProcessor operacionMenuProcessor) {
    this.clienteCreateProcessor = clienteInputProcessor;
    this.clienteModifyProcessor = clienteModifyProcessor;
    this.clienteDeleteProcessor = clienteDeleteProcessor;
    this.cuentaCreateProcessor = cuentaInputProcessor;
    this.operacionMenuProcessor = operacionMenuProcessor;
  }

  public MenuInputProcessor() {
    this.clienteCreateProcessor = new ClienteCreateProcessor();
    this.clienteModifyProcessor = new ClienteModifyProcessor();
    this.clienteDeleteProcessor = new ClienteDeleteProcessor();
    this.cuentaCreateProcessor = new CuentaCreateProcessor();
    this.operacionMenuProcessor = new OperacionMenuProcessor(null);
  }

  public void renderMenu() {
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
            clienteCreateProcessor.ingresarCliente();
            break;
          }
        case 2:
          {
            clienteModifyProcessor.modifyCliente();
            break;
          }
        case 3:
          {
            clienteDeleteProcessor.deleteCliente();
            break;
          }
        case 4:
          {
            cuentaCreateProcessor.createCuenta();
            break;
          }
        case 5:
          {
            Cliente cliente;
            try {
              cliente = this.getClienteByDni();
            } catch (ClienteNoExistsException e) {
              System.out.println(e.getMessage());
              return;
            }
            Cuenta cuenta = this.getCuentaId(cliente.getCuentas());
            if (Objects.isNull(cuenta)) {
              break;
            }
            operacionMenuProcessor.setCuenta(cuenta);
            operacionMenuProcessor.renderMenu();
            break;
          }
        case 6:
          exit = true;
          break;
        default:
          System.out.println("Opción no válida, intente nuevamente.");
      }
      clearScreen();
    }
  }
}
