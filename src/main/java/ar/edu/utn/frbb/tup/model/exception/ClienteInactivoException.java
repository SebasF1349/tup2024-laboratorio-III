package ar.edu.utn.frbb.tup.model.exception;

public class ClienteInactivoException extends BadRequestException {
  public ClienteInactivoException(String message) {
    super(message, 112);
  }
}
