package ar.edu.utn.frbb.tup.model.exception;

public abstract class ServiceUnavailableException extends ServerException {
  public ServiceUnavailableException(String message, Integer internalCode) {
    super(message, 503, internalCode);
  }
}
