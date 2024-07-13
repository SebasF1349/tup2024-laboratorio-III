package ar.edu.utn.frbb.tup.model.exception;

public abstract class NotFoundException extends ClientException {
  public NotFoundException(String message, Integer internalCode) {
    super(message, 404, internalCode);
  }
}
