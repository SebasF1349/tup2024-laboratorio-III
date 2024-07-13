package ar.edu.utn.frbb.tup.model.exception;

public class ClienteMenorDeEdadException extends BadRequestException {
  public ClienteMenorDeEdadException(String message) {
    super(message, 111);
  }
}
