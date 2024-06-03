package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.service.CuentaService;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
      Set<Cuenta> cuentas = new HashSet<>();
      for (Cuenta cuentaCliente : cliente.getCuentas()) {
        if (!cuentaCliente.equals(this.cuenta)) {
          cuentas.add(cuentaCliente);
        }
      }
      cuentas.remove(this.cuenta);
      Cuenta cuentaDestino = this.getCuentaId(cuentas);
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
