package ar.edu.utn.frbb.tup.model.exception;

public class CuentaInactivaException extends BadRequestException {
  public CuentaInactivaException(String message) {
    super(message, 114);
  }
}
