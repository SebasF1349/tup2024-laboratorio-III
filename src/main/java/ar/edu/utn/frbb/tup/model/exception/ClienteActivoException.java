package ar.edu.utn.frbb.tup.model.exception;

public class ClienteActivoException extends BadRequestException {
  public ClienteActivoException(String message) {
    super(message, 113);
  }
}
