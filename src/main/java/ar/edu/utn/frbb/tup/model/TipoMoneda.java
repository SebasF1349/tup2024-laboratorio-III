package ar.edu.utn.frbb.tup.model;

public enum TipoMoneda {
  PESOS_ARGENTINOS("P"),
  DOLARES_AMERICANOS("D");

  private final String descripcion;

  TipoMoneda(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static TipoMoneda fromString(String text) {
    for (TipoMoneda tipo : TipoMoneda.values()) {
      if (tipo.descripcion.equalsIgnoreCase(text)) {
        return tipo;
      }
    }
    throw new IllegalArgumentException(
        "No se pudo encontrar un MonedaCuenta con la descripción: " + text);
  }

  public String getSymbol() {
    switch (this) {
      case PESOS_ARGENTINOS:
        return "$";
      case DOLARES_AMERICANOS:
        return "us$";
      default:
        return "Moneda not found";
    }
  }

  @Override
  public String toString() {
    switch (this) {
      case PESOS_ARGENTINOS:
        return "Pesos Argentinos";
      case DOLARES_AMERICANOS:
        return "Dólares Americanos";
      default:
        return null;
    }
  }
}
