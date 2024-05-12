package ar.edu.utn.frbb.tup.model;

public class ConsultaSaldo extends Operacion {
  public String imprimir(double saldo, MonedaCuenta moneda) {
    return "Saldo de la cuenta: " + moneda.getSymbol() + saldo;
  }
}
