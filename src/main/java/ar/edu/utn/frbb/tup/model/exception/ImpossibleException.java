package ar.edu.utn.frbb.tup.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpossibleException extends InternalServerErrorException {
  private static final Logger logger = LoggerFactory.getLogger(ImpossibleException.class);

  public ImpossibleException(String message) {
    super("Error irrecuperable", 100);
    logger.error(message);
  }
}
