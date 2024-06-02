package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.MonedaCuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;

public class CuentaCreateProcessor extends BaseInputProcessor {
  public Cuenta createCuenta() {
    clearScreen();

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

    Cuenta cuenta = new Cuenta(tipoCuenta, moneda);

    clearScreen();
    return cuenta;
  }
}
