package ar.edu.utn.frbb.tup.model.exception;

public abstract class BaseCustomException extends Exception {
  private Integer statusCode;
  private Integer internalCode;

  public BaseCustomException(String message, Integer statusCode, Integer internalCode) {
    super(message);
    this.statusCode = statusCode;
    this.internalCode = internalCode;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public Integer getInternalCode() {
    return internalCode;
  }

  public void setInternalCode(Integer internalCode) {
    this.internalCode = internalCode;
  }

  public Integer getCode() {
    return Integer.valueOf(String.valueOf(statusCode) + String.valueOf(internalCode));
  }
}
