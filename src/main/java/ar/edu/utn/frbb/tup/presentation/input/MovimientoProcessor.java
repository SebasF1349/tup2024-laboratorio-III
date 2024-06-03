package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Clientes;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MovimientoProcessor extends ClienteProcessor {
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

  public Transferencia createTransferencia(Cuenta cuenta) {
    boolean esCuentaPropia = this.getBooleanInput("¿Desea transferir a una cuenta propia?");
    Clientes clientes = Clientes.getInstance();
    Cliente cliente = clientes.getClienteByCuentaId(cuenta.getNumeroCuenta());
    String numeroCuentaDestino;
    if (esCuentaPropia) {
      Set<Cuenta> cuentas = new HashSet<>();
      for (Cuenta cuentaCliente : cliente.getCuentas()) {
        if (!cuentaCliente.equals(cuenta)) {
          cuentas.add(cuentaCliente);
        }
      }
      cuentas.remove(cuenta);
      if (cuentas.size() == 0) {
        System.out.println("El Cliente no posee otras Cuentas para transferir");
        scanner.nextLine();
        return null;
      }
      Cuenta cuentaDestino = this.getCuentaId(cuentas);
      if (Objects.isNull(cuentaDestino)) {
        return null;
      }
      numeroCuentaDestino = cuentaDestino.getNumeroCuenta();
    } else {
      numeroCuentaDestino = this.getCuentaDestino();
      if (Objects.isNull(clientes.getClienteByCuentaId(numeroCuentaDestino))) {
        System.out.println("La cuenta de destino no existe.");
        scanner.nextLine();
        return null;
      }
    }
    double monto = this.getMonto();
    return new Transferencia(monto, esCuentaPropia, numeroCuentaDestino, false, this.cuenta);
  }
}
