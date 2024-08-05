package ar.edu.utn.frbb.tup.controller.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorDetailed extends ApiError {
  @Schema(
      description = "Lista de errores",
      example = "{ nombre = \"No puede estar vacío\" }",
      requiredMode = RequiredMode.REQUIRED)
  private Map<String, String> errors;

  public ApiErrorDetailed(Integer errorCode, String errorMessage, Map<String, String> errors) {
    super(errorCode, errorMessage);
    this.errors = errors;
  }

  public ApiErrorDetailed(Integer errorCode, String errorMessage) {
    super(errorCode, errorMessage);
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
