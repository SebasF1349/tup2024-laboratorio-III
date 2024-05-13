package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Clientes;
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
            System.out.println(consultaSaldo.imprimir(cuenta.getSaldo(), cuenta.getMoneda()));
            scanner.nextLine();
            break;
          }
        case 2:
          {
            MovimientoProcessor movimientoProcessor = new MovimientoProcessor();
            Retiro retiro = movimientoProcessor.createRetiro();
            try {
              cuenta.addMovimiento(retiro);
            } catch (IllegalArgumentException e) {
              System.err.println(e.getMessage());
              scanner.nextLine();
            }
            break;
          }
        case 3:
          {
            MovimientoProcessor movimientoProcessor = new MovimientoProcessor();
            Deposito deposito = movimientoProcessor.createDeposito();
            try {
              cuenta.addMovimiento(deposito);
            } catch (IllegalArgumentException e) {
              System.err.println(e.getMessage());
              scanner.nextLine();
            }
            break;
          }
        case 4:
          {
            MovimientoProcessor movimientoProcessor = new MovimientoProcessor();
            Transferencia transferencia = movimientoProcessor.createTransferencia(cuenta);
            try {
              cuenta.addMovimiento(transferencia);
            } catch (IllegalArgumentException e) {
              System.err.println(e.getMessage());
              scanner.nextLine();
            }
            Transferencia transferenciaRecibido =
                new Transferencia(
                    transferencia.getMonto(),
                    transferencia.isEsCuentaPropia(),
                    cuenta.getId(),
                    true);
            Cuenta clienteReceptor =
                Clientes.getInstance().getCuentaById(transferencia.getIdCuentaDestino());
            try {
              clienteReceptor.addMovimiento(transferenciaRecibido);
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
          System.out.println("Opción no implementada");
      }
      clearScreen();
    }
  }
}
