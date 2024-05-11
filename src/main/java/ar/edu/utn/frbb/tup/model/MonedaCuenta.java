package ar.edu.utn.frbb.tup.model;

public enum MonedaCuenta {
  PESOS_ARGENTINOS("P"),
  DOLARES_AMERICANOS("D");

  private final String descripcion;

  MonedaCuenta(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static MonedaCuenta fromString(String text) {
    for (MonedaCuenta tipo : MonedaCuenta.values()) {
      if (tipo.descripcion.equalsIgnoreCase(text)) {
        return tipo;
      }
    }
    throw new IllegalArgumentException(
        "No se pudo encontrar un MonedaCuenta con la descripción: " + text);
  }
}