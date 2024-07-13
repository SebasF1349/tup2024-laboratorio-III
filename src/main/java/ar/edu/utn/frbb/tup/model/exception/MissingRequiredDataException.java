package ar.edu.utn.frbb.tup.model.exception;

public class MissingRequiredDataException extends BadRequestException {
  public MissingRequiredDataException(String message) {
    super(message, 102);
  }
}
