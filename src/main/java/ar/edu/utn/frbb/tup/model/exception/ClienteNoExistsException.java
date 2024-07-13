package ar.edu.utn.frbb.tup.model.exception;

public class ClienteNoExistsException extends NotFoundException {
  public ClienteNoExistsException(String message) {
    super(message, 101);
  }
}
