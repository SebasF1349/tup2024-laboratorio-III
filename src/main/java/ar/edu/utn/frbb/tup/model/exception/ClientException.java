package ar.edu.utn.frbb.tup.model.exception;

public abstract class ClientException extends BaseCustomException {
  public ClientException(String message, Integer statusCode, Integer internalCode) {
    super(message, statusCode, internalCode);
  }
}
