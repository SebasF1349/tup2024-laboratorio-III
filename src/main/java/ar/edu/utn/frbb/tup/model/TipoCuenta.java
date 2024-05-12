package ar.edu.utn.frbb.tup.model;

public enum TipoCuenta {
  CUENTA_CORRIENTE("C"),
  CAJA_AHORROS("A");

  private final String descripcion;

  TipoCuenta(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static TipoCuenta fromString(String text) {
    for (TipoCuenta tipo : TipoCuenta.values()) {
      if (tipo.descripcion.equalsIgnoreCase(text)) {
        return tipo;
      }
    }
    throw new IllegalArgumentException(
        "No se pudo encontrar un TipoCuenta con la descripción: " + text);
  }

  @Override
  public String toString() {
    switch (this) {
      case CUENTA_CORRIENTE:
        return "Cuenta Corriente";
      case CAJA_AHORROS:
        return "Caja de Ahorros";
      default:
        return null;
    }
  }
}
