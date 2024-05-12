package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.MonedaCuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import java.util.Objects;

public class CuentaCreateProcessor extends BaseInputProcessor {
  public Cliente modifyClienteByDni(Banco banco) {
    String dni = this.getStringInput("Ingrese el dni del Clienta asociado a la Cuenta:");
    Cliente cliente = banco.getClienteByDni(dni);
    if (Objects.isNull(cliente)) {
      System.out.println("Cliente no encontrado.");
      scanner.nextLine();
      return null;
    }
    return cliente;
  }

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
