package ar.edu.utn.frbb.tup.model.exception;

public class CorruptedDataInDbException extends InternalServerErrorException {

  public CorruptedDataInDbException(String message) {
    super("No pudo realizarse la operación por un problema interno", 101);
    logger.error("Corrupted data in DB: " + message);
  }
}
