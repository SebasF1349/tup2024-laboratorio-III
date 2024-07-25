package ar.edu.utn.frbb.tup.model.exception;

public class BanelcoErrorException extends ServiceUnavailableException {
  public BanelcoErrorException(String message, Integer banelcoCode) {
    super(message, Integer.valueOf(String.valueOf(101) + String.valueOf(banelcoCode)));
  }
}
