package ar.edu.utn.frbb.tup.model.exception;

public class MontoInsuficienteException extends BadRequestException {
  public MontoInsuficienteException(String message) {
    super(message, 131);
  }
}
