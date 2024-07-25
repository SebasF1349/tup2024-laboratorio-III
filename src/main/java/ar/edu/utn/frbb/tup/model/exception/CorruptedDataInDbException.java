package ar.edu.utn.frbb.tup.model.exception;

public class CorruptedDataInDbException extends InternalServerErrorException {
  public CorruptedDataInDbException(String message) {
    super(message, 101);
  }
}
