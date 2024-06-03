package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;

public class OperacionProcessor extends BusinessProcessor {
  Cuenta cuenta;

  public Cuenta getCuenta() {
    return cuenta;
  }

  public void setCuenta(Cuenta cuenta) {
    this.cuenta = cuenta;
  }

  public void addMovimiento(Movimiento movimiento) {
    try {
      cuenta.addMovimiento(movimiento);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      scanner.nextLine();
    }
  }
}
