package ar.edu.utn.frbb.tup.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorruptedDataInDbException extends InternalServerErrorException {
  private static final Logger logger = LoggerFactory.getLogger(CorruptedDataInDbException.class);

  public CorruptedDataInDbException(String message) {
    super("No pudo realizarse la operación por un problema interno", 101);
    logger.error(message);
  }
}
