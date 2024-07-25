package ar.edu.utn.frbb.tup.model.exception;

public class InternalServerErrorException extends ServerException {
  public InternalServerErrorException(String message, Integer internalCode) {
    super(message, 500, internalCode);
  }
}
