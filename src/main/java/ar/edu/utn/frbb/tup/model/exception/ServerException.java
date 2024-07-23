package ar.edu.utn.frbb.tup.model.exception;

public abstract class ServerException extends BaseCustomException {
  public ServerException(String message, Integer statusCode, Integer internalCode) {
    super(message, statusCode, internalCode);
  }
}
