package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.MovimientoControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Movimiento Endpoints")
@RequestMapping("/api/")
public class MovimientoController {

  @Autowired private MovimientoService movimientoService;
  @Autowired private MovimientoControllerValidator movimientoControllerValidator;

  @Operation(summary = "Realizar Transferencia")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Transferencia realizada"),
        @ApiResponse(responseCode = "400", description = "Monto Insuficiente\t\nMonedas Distintas"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "503", description = "Error con el servicio de Banelco"),
      })
  @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
  public TransferenciaResponseDto realizarTransferencia(
      @Valid
          @RequestBody
          @Parameter(name = "Transferencia", description = "Datos de la Transferencia")
          TransferenciaRequestDto transferenciaDto)
      throws WrongInputDataException,
          CuentaNoExistsException,
          MontoInsuficienteException,
          MonedasDistintasException,
          CorruptedDataInDbException,
          BanelcoErrorException,
          ImpossibleException {
    movimientoControllerValidator.validate(transferenciaDto);
    return movimientoService.realizarTransferencia(transferenciaDto);
  }

  @Operation(summary = "Realizar Deposito")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Deposito realizada"),
        @ApiResponse(
            responseCode = "400",
            description = "Información ingresada incorrecta\t\nMonedas Distintas"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @PostMapping(value = "/deposit", produces = MediaType.APPLICATION_JSON_VALUE)
  public DepositoResponseDto realizarDeposito(
      @Valid @RequestBody @Parameter(name = "Deposito", description = "Datos de la Deposito")
          DepositoRequestDto depositoRequestDto)
      throws WrongInputDataException,
          MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    movimientoControllerValidator.validate(depositoRequestDto);
    return movimientoService.realizarDeposito(depositoRequestDto);
  }

  @Operation(summary = "Realizar Retiro")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Retiro realizada"),
        @ApiResponse(
            responseCode = "400",
            description =
                "Información ingresada incorrecta\t\n"
                    + "Monedas Distintas\t\n"
                    + "Saldo disponible insuficiente"),
        @ApiResponse(responseCode = "404", description = "Cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @PostMapping(value = "/withdrawal", produces = MediaType.APPLICATION_JSON_VALUE)
  public RetiroResponseDto realizarRetiro(
      @Valid @RequestBody @Parameter(name = "Retiro", description = "Datos de la Retiro")
          RetiroRequestDto retiroRequestDto)
      throws WrongInputDataException,
          MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          MontoInsuficienteException {
    movimientoControllerValidator.validate(retiroRequestDto);
    return movimientoService.realizarRetiro(retiroRequestDto);
  }
}
