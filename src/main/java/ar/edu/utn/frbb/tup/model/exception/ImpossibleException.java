package ar.edu.utn.frbb.tup.model.exception;

public class ImpossibleException extends ServerException {
  public ImpossibleException() {
    super("Error irrecuperable", 500, 100);
    // TODO: add logging
  }
}
