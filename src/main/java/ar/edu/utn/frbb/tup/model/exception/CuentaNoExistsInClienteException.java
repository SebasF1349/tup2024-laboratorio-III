package ar.edu.utn.frbb.tup.model.exception;

public class CuentaNoExistsInClienteException extends NotFoundException {
  public CuentaNoExistsInClienteException(String message) {
    super(message, 103);
  }
}
