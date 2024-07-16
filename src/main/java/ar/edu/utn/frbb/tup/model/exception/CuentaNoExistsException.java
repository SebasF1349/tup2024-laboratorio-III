package ar.edu.utn.frbb.tup.model.exception;

public class CuentaNoExistsException extends NotFoundException {
  public CuentaNoExistsException(String message) {
    super(message, 102);
  }
}
