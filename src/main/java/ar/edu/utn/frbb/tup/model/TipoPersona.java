package ar.edu.utn.frbb.tup.model;

public enum TipoPersona {
  PERSONA_FISICA("F"),
  PERSONA_JURIDICA("J");

  private final String descripcion;

  TipoPersona(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static TipoPersona fromString(String text) {
    for (TipoPersona tipo : TipoPersona.values()) {
      if (tipo.descripcion.equalsIgnoreCase(text) || tipo.toString().equalsIgnoreCase(text)) {
        return tipo;
      }
    }
    throw new IllegalArgumentException(
        "No se pudo encontrar un TipoPersona con la descripción: " + text);
  }

  @Override
  public String toString() {
    switch (this) {
      case PERSONA_FISICA:
        return "Fisica";
      case PERSONA_JURIDICA:
        return "Juridica";
      default:
        return null;
    }
  }
}
