package ar.edu.utn.frbb.tup.model;

public enum TipoMovimiento {
  DEBITO("D"),
  CREDITO("C");

  private final String descripcion;

  TipoMovimiento(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static TipoMovimiento fromString(String text) {
    for (TipoMovimiento tipo : TipoMovimiento.values()) {
      if (tipo.descripcion.equalsIgnoreCase(text)) {
        return tipo;
      }
    }
    throw new IllegalArgumentException(
        "No se pudo encontrar un TipoMovimiento con la descripción: " + text);
  }
}
