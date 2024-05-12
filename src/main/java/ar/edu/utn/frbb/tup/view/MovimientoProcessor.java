package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;

public class MovimientoProcessor extends BaseInputProcessor {
  private double getMonto() {
    System.out.println("Ingrese monto:");
    String montoStr = scanner.nextLine();
    boolean montoValido = false;
    double monto = 0;
    while (!montoValido) {
      try {
        monto = Double.parseDouble(montoStr);
        montoValido = true;
      } catch (Exception e) {
        System.out.println("Monto inválido. Ingrese el monto como un número:");
      }
    }
    return monto;
  }

  private int getCuentaDestino() {
    System.out.println("Ingrese el id de la cuenta destino:");
    String cuentaStr = scanner.nextLine();
    boolean cuentaValida = false;
    int cuentaId = 0;
    while (!cuentaValida) {
      try {
        // Note: Falta revisar que el id existe
        cuentaId = Integer.parseInt(cuentaStr);
        cuentaValida = true;
      } catch (Exception e) {
        System.out.println("Id de Cuenta inválida. Ingrese el id como un número:");
      }
    }
    return cuentaId;
  }

  public Retiro createRetiro() {
    double monto = this.getMonto();
    return new Retiro(monto);
  }

  public Deposito createDeposito() {
    double monto = this.getMonto();
    return new Deposito(monto);
  }

  public Transferencia createTransferencia() {
    double monto = this.getMonto();
    boolean esCuentaPropia = this.getBooleanInput("¿Desea transferir a una cuenta propia?");
    int idCuentaDestino = this.getCuentaDestino();
    return new Transferencia(monto, esCuentaPropia, idCuentaDestino);
  }
}
