package ar.edu.utn.frbb.tup.model.exception;

public class CuentaActivaException extends BadRequestException {
  public CuentaActivaException(String message) {
    super(message, 115);
  }
}
