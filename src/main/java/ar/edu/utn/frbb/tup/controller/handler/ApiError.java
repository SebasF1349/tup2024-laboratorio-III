package ar.edu.utn.frbb.tup.controller.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
  // TODO: PDF pide devolver errores con keys 'estado' y 'mensaje'
  private Integer errorCode;
  private String errorMessage;
  private Map<String, String> errors;

  public ApiError() {}

  public ApiError(Integer errorCode, String errorMessage, Map<String, String> errors) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.errors = errors;
  }

  public ApiError(Integer errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }

  public void addError(String errorParam, String error) {
    this.errors.put(errorParam, error);
  }
}
