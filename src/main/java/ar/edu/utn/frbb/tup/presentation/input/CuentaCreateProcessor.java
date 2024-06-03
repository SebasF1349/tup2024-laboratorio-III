package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.MonedaCuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import org.springframework.stereotype.Component;

@Component
public class CuentaCreateProcessor extends CuentaProcessor {
  public void createCuenta() {
    clearScreen();

    Cliente cliente;
    try {
      cliente = this.getClienteByDni();
    } catch (ClienteNoExistsException e) {
      System.out.println(e.getMessage());
      return;
    }

    String tipoCuentaStr =
        this.getEnumInput(
            "Ingrese el tipo de Cuenta - Cuenta Corriente[C] o Caja de Ahorros[A]:", "C", "A");
    TipoCuenta tipoCuenta = TipoCuenta.fromString(tipoCuentaStr);

    String monedaStr =
        this.getEnumInput(
            "Ingrese la moneda de la Cuenta - Pesos Argentinos[P] o Dólares Americanos[D]:",
            "P",
            "D");
    MonedaCuenta moneda = MonedaCuenta.fromString(monedaStr);

    Cuenta cuenta = new Cuenta(tipoCuenta, moneda, cliente);

    try {
      cuentaService.darDeAltaCuenta(cuenta);
    } catch (CuentaAlreadyExistsException e) {
      System.out.println();
      System.out.println(e.getMessage());
      System.out.println();
    }

    clearScreen();
  }
}
