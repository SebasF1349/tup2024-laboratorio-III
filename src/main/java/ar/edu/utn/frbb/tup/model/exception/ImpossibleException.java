package ar.edu.utn.frbb.tup.model.exception;

public class ImpossibleException extends InternalServerErrorException {

  public ImpossibleException(String message) {
    super("Error irrecuperable", 100);
    logger.error("Impossible Exception: " + message);
  }
}
