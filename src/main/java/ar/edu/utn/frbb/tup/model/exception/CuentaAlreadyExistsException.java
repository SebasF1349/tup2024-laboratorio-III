package ar.edu.utn.frbb.tup.model.exception;

public class CuentaAlreadyExistsException extends BadRequestException {
  public CuentaAlreadyExistsException(String message) {
    super(message, 120);
  }
}
