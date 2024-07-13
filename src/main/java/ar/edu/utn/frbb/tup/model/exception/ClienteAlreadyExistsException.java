package ar.edu.utn.frbb.tup.model.exception;

public class ClienteAlreadyExistsException extends BadRequestException {
  public ClienteAlreadyExistsException(String message) {
    super(message, 110);
  }
}
