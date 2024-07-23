package ar.edu.utn.frbb.tup.model.exception;

public class MonedasDistintasException extends BadRequestException {

  public MonedasDistintasException(String message) {
    super(message, 132);
  }
}
