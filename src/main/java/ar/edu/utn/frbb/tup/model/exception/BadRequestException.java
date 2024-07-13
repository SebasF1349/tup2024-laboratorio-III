package ar.edu.utn.frbb.tup.model.exception;

public abstract class BadRequestException extends ClientException {
  public BadRequestException(String message, Integer internalCode) {
    super(message, 400, internalCode);
  }
}
