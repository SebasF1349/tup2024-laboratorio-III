package ar.edu.utn.frbb.tup.model.exception;

public class BanelcoErrorException extends ServerException {
  public BanelcoErrorException(String message, Integer statusCode) {
    super(message, statusCode, 901);
  }
}
