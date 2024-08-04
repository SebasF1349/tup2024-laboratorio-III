package ar.edu.utn.frbb.tup.model;

public enum TipoTransaccion {
  DEBITO("D"),
  CREDITO("C");

  private final String descripcion;

  TipoTransaccion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static TipoTransaccion fromString(String text) {
    for (TipoTransaccion tipo : TipoTransaccion.values()) {
      if (tipo.descripcion.equalsIgnoreCase(text)) {
        return tipo;
      }
    }
    throw new IllegalArgumentException(
        "No se pudo encontrar un TipoTransaccion con la descripción: " + text);
  }

  @Override
  public String toString() {
    switch (this) {
      case DEBITO:
        return "Debito";
      case CREDITO:
        return "Credito";
      default:
        return null;
    }
  }
}
