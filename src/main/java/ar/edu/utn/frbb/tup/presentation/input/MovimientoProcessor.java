package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.service.CuentaService;
import java.util.Objects;

public class MovimientoProcessor extends OperacionProcessor {
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

  private String getCuentaDestino() {
    System.out.println("Ingrese el id de la cuenta destino:");
    boolean cuentaValida = false;
    String numeroCuenta = null;
    CuentaService cuentaService = new CuentaService();
    while (!cuentaValida) {
      numeroCuenta = scanner.nextLine();
      if (numeroCuenta != null && cuentaService.find(numeroCuenta) != null) {
        cuentaValida = true;
      } else {
        System.out.println("Id de Cuenta inválida. Ingrese el id como un número:");
      }
    }
    return numeroCuenta;
  }

  public Retiro createRetiro() {
    double monto = this.getMonto();
    return new Retiro(monto, this.cuenta);
  }

  public Deposito createDeposito() {
    double monto = this.getMonto();
    return new Deposito(monto, this.cuenta);
  }

  public Transferencia createTransferencia() {
    boolean esCuentaPropia = this.getBooleanInput("¿Desea transferir a una cuenta propia?");
    Cliente cliente = this.cuenta.getTitular();
    String numeroCuentaDestino;
    if (esCuentaPropia) {
      Cuenta cuentaDestino = this.getCuentaId(cliente.getCuentasFiltrada(this.cuenta));
      if (Objects.isNull(cuentaDestino)) {
        return null;
      }
      numeroCuentaDestino = cuentaDestino.getNumeroCuenta();
    } else {
      numeroCuentaDestino = this.getCuentaDestino();
    }
    double monto = this.getMonto();
    return new Transferencia(monto, esCuentaPropia, numeroCuentaDestino, false, this.cuenta);
  }
}
