package ar.edu.utn.frbb.tup.model.exception;

public class WrongInputDataException extends BadRequestException {
  public WrongInputDataException(String message) {
    super(message, 101);
  }
}
