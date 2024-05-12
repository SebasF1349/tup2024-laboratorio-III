package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.ConsultaSaldo;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import java.util.Arrays;
import java.util.List;

public class OperacionMenuProcessor extends BaseInputProcessor {
  private boolean exit = false;

  public void renderMenu(Cuenta cuenta) {

    List<String> menuOptions =
        Arrays.asList("Consulta Saldo", "Retiro de Dinero", "Deposito de Dinero", "Transferencia");

    while (!exit) {
      int choice =
          this.getMultipleOptionsInput("Elija la operación que desea realizar.", menuOptions);

      switch (choice) {
        case 1:
          {
            ConsultaSaldo consultaSaldo = new ConsultaSaldo();
            System.out.println(consultaSaldo.imprimir(cuenta.getSaldo(), cuenta.getMoneda()));
            break;
          }
        case 2:
          {
            MovimientoProcessor movimientoProcessor = new MovimientoProcessor();
            Retiro retiro = movimientoProcessor.createRetiro();
            cuenta.addMovimiento(retiro);
            break;
          }
        case 3:
          {
            MovimientoProcessor movimientoProcessor = new MovimientoProcessor();
            Deposito deposito = movimientoProcessor.createDeposito();
            cuenta.addMovimiento(deposito);
            break;
          }
        case 4:
          {
            MovimientoProcessor movimientoProcessor = new MovimientoProcessor();
            Transferencia transferencia = movimientoProcessor.createTransferencia();
            cuenta.addMovimiento(transferencia);
            break;
          }
        case 5:
          exit = true;
          break;
        default:
          System.out.println("Opción inválida. Por favor seleccione 1-4.");
      }
      clearScreen();
    }
  }
}