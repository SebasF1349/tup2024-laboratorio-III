package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.BadRequestException;
import ar.edu.utn.frbb.tup.model.exception.NotFoundException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NotFoundException.class})
  protected ResponseEntity<Object> handleClienteNotFound(NotFoundException ex, WebRequest request) {
    ApiError error = new ApiError(ex.getCode(), ex.getMessage());
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {BadRequestException.class})
  protected ResponseEntity<Object> handleClienteAlreadyExists(
      BadRequestException ex, WebRequest request) {
    ApiError error = new ApiError(ex.getCode(), ex.getMessage());
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  // NOTE: NOT CHECKED FROM HERE

  @ExceptionHandler(
      value = {TipoCuentaAlreadyExistsException.class, IllegalArgumentException.class})
  protected ResponseEntity<Object> handleMateriaNotFound(Exception ex, WebRequest request) {
    String exceptionMessage = ex.getMessage();
    ApiError error = new ApiError();
    error.setErrorMessage(exceptionMessage);
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String exceptionMessage = ex.getMessage();
    ApiError error = new ApiError();
    error.setErrorCode(1234);
    error.setErrorMessage(exceptionMessage);
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  // NOTE: UNTIL HERE NOT CHECKED

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    if (body == null) {
      ApiError error = new ApiError();
      error.setErrorMessage(ex.getMessage());
      body = error;
    }

    return new ResponseEntity<Object>(body, headers, status);
  }
}
