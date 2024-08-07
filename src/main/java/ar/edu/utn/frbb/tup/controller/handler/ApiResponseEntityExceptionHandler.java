package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.BadRequestException;
import ar.edu.utn.frbb.tup.model.exception.InternalServerErrorException;
import ar.edu.utn.frbb.tup.model.exception.NotFoundException;
import ar.edu.utn.frbb.tup.model.exception.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NotFoundException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "404",
            description = "No Encontrado",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleClienteNotFound(NotFoundException ex, WebRequest request) {
    ApiError error = new ApiError(ex.getCode(), ex.getMessage());
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {BadRequestException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleClienteAlreadyExists(
      BadRequestException ex, WebRequest request) {
    ApiError error = new ApiError(ex.getCode(), ex.getMessage());
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {InternalServerErrorException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "500",
            description = "Error del Servidor",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleBanelcoError(
      InternalServerErrorException ex, WebRequest request) {
    ApiError error = new ApiError(ex.getCode(), ex.getMessage());
    return handleExceptionInternal(
        ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(value = {ServiceUnavailableException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "503",
            description = "Servicio No Disponible",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleBanelcoError(
      ServiceUnavailableException ex, WebRequest request) {
    ApiError error = new ApiError(ex.getCode(), ex.getMessage());
    return handleExceptionInternal(
        ex, error, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String errorMessage = ex.getName() + " debe ser de tipo " + ex.getRequiredType().getName();
    ApiError error = new ApiError(400001, errorMessage);
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    ApiError error = new ApiError(400103, ex.getMessage());
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorDetailed.class))
            })
      })
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.put(
          violation.getRootBeanClass().getName() + " " + violation.getPropertyPath(),
          violation.getMessage());
    }

    ApiError apiError = new ApiErrorDetailed(400104, "Campos fallaron validación", errors);
    return handleExceptionInternal(
        ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @Override
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String errorMessage =
        ex.getPropertyName() + " debe ser de tipo " + ex.getRequiredType().getName();
    ApiError apiError = new ApiError(400002, errorMessage);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    String error = "No se encuentra el parámetro " + ex.getParameterName();
    ApiError apiError = new ApiError(400105, error);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorDetailed.class))
            })
      })
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    ApiError apiError = new ApiErrorDetailed(400104, "Campos fallaron validación", errors);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ApiError error = new ApiError(400003, ex.getMessage());
    return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Datos Incorrectos",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    String msg =
        String.format(
            "No se encontró el método %s para el endpoint %s",
            ex.getHttpMethod(), ex.getRequestURL());
    ApiError error = new ApiError(404001, msg);
    return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "405",
            description = "Metodo No Soportado",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ApiError error = new ApiError(405101, "Método no permitido en este endpoint");
    return handleExceptionInternal(ex, error, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
  }

  @ExceptionHandler(value = {Exception.class})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "500",
            description = "Error Del Servidor",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    ApiError error = new ApiError(500000, ex.getMessage());
    return handleExceptionInternal(
        ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

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
