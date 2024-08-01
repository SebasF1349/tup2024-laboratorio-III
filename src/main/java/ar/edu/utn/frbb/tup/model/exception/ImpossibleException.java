package ar.edu.utn.frbb.tup.model.exception;

public class ImpossibleException extends InternalServerErrorException {
  public ImpossibleException() {
    super("Error irrecuperable", 100);
    // TODO: add logging
  }
}
