package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaActivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaInactivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Cuenta Endpoints")
@RequestMapping("/api/cuenta")
public class CuentaController {

  @Autowired private CuentaService cuentaService;
  @Autowired private CuentaControllerValidator cuentaValidator;

  @Operation(summary = "Obtener Cuenta")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cuenta obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public CuentaResponseDto obtenerCuenta(
      @PathVariable @Parameter(description = "Id de la Cuenta", example = "123456") long id)
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    return cuentaService.buscarCuentaPorId(id);
  }

  @Operation(summary = "Crear Cuenta")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Cuenta obtenido exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description =
                "Información no válida\t\n"
                    + "Cuenta no soportada\t\n"
                    + "Cliente ya posee cuenta del mismo tipo\t\n"
                    + "Cuenta inactiva\t\n"
                    + "Cliente inactivo"),
        @ApiResponse(responseCode = "404", description = "Cliente no existe\t\nCuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CuentaResponseDto> crearCuenta(
      @Valid @RequestBody @Parameter(name = "Cuenta", description = "Datos de la Cuenta")
          CuentaRequestDto cuentaRequestDto)
      throws WrongInputDataException,
          CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CorruptedDataInDbException,
          ClienteInactivoException,
          CuentaInactivaException,
          ImpossibleException {
    cuentaValidator.validate(cuentaRequestDto);
    CuentaResponseDto cuentaResponse = cuentaService.darDeAltaCuenta(cuentaRequestDto);
    return new ResponseEntity<CuentaResponseDto>(
        cuentaResponse, new HttpHeaders(), HttpStatus.CREATED);
  }

  @Operation(summary = "Inactivar Cuenta")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cuenta inactivada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cuenta ya inactiva\t\n"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public CuentaResponseDto eliminarCuenta(
      @PathVariable @Parameter(description = "Id de la Cuenta", example = "123456") long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    return cuentaService.eliminarCuenta(id);
  }

  @Operation(summary = "Activar Cuenta")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cuenta inactivada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cuenta ya activa\t\n"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public CuentaResponseDto activarCuenta(
      @PathVariable @Parameter(description = "Id de la Cuenta", example = "123456") long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaActivaException {
    return cuentaService.activarCuenta(id);
  }

  @Operation(summary = "Obtener transacciones en Cuenta")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cuenta inactivada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cuenta ya inactiva\t\n"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @GetMapping(value = "/{id}/transacciones", produces = MediaType.APPLICATION_JSON_VALUE)
  public CuentaMovimientosResponseDto obtenerTransaccionesEnCuenta(
      @PathVariable @Parameter(description = "Id de la Cuenta", example = "123456") long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    return cuentaService.buscarTransaccionesDeCuentaPorId(id);
  }
}
