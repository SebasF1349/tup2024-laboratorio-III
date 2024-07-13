package ar.edu.utn.frbb.tup.model.exception;

public class TipoCuentaAlreadyExistsException extends BadRequestException {
  public TipoCuentaAlreadyExistsException(String message) {
    super(message, 123);
  }
}
