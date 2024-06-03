package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.ConsultaSaldo;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.service.CuentaService;
import java.util.Arrays;
import java.util.List;

public class OperacionMenuProcessor extends OperacionProcessor {
  MovimientoProcessor movimientoProcessor;
  private boolean exit = false;

  public OperacionMenuProcessor(MovimientoProcessor movimientoProcessor) {
    this.movimientoProcessor = movimientoProcessor;
  }

  public void renderMenu() {

    List<String> menuOptions =
        Arrays.asList(
            "Consulta Saldo",
            "Retiro de Dinero",
            "Deposito de Dinero",
            "Transferencia",
            "Volver al menú anterior");

    while (!exit) {
      int choice =
          this.getMultipleOptionsInput("Elija la operación que desea realizar.", menuOptions);

      switch (choice) {
        case 1:
          {
            ConsultaSaldo consultaSaldo = new ConsultaSaldo();
            System.out.println(consultaSaldo.imprimir(cuenta.getBalance(), cuenta.getMoneda()));
            scanner.nextLine();
            break;
          }
        case 2:
          {
            Retiro retiro = movimientoProcessor.createRetiro();
            this.addMovimiento(retiro);
            break;
          }
        case 3:
          {
            Deposito deposito = movimientoProcessor.createDeposito();
            this.addMovimiento(deposito);
            break;
          }
        case 4:
          {
            Transferencia transferencia = movimientoProcessor.createTransferencia();
            this.addMovimiento(transferencia);
            Transferencia transferenciaRecibido =
                new Transferencia(
                    transferencia.getMonto(),
                    transferencia.isEsCuentaPropia(),
                    cuenta.getNumeroCuenta(),
                    true,
                    cuenta);
            CuentaService cuentaService = new CuentaService();
            Cuenta cuentaDestino = cuentaService.find(transferencia.getNumeroCuentaDestino());
            try {
              cuentaDestino.addMovimiento(transferenciaRecibido);
            } catch (IllegalArgumentException e) {
              System.err.println(e.getMessage());
              scanner.nextLine();
            }
            break;
          }
        case 5:
          exit = true;
          break;
        default:
          System.out.println("Opción no válida, intente nuevamente.");
      }
      clearScreen();
    }
  }
}
