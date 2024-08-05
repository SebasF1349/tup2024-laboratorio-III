package ar.edu.utn.frbb.tup.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServerException extends BaseCustomException {
  protected final Logger logger = LoggerFactory.getLogger(ImpossibleException.class);

  public ServerException(String message, Integer statusCode, Integer internalCode) {
    super(message, statusCode, internalCode);
  }
}
