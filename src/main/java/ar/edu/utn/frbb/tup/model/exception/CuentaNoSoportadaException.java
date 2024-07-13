package ar.edu.utn.frbb.tup.model.exception;

public class CuentaNoSoportadaException extends BadRequestException {
  public CuentaNoSoportadaException(String errorMsg) {
    super(errorMsg, 121);
  }
}
