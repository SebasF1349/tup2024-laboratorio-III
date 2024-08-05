package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ClienteControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteActivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.ClienteService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Cliente Endpoints")
@RequestMapping("/api/cliente")
public class ClienteController {

  @Autowired private ClienteService clienteService;

  @Autowired private ClienteControllerValidator clienteControllerValidator;

  @Operation(summary = "Obtener Cliente")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cliente obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no existe")
      })
  @GetMapping(value = "/{dni}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ClienteResponseDto obtenerCliente(
      @PathVariable @Parameter(description = "DNI del Cliente", example = "12345678") long dni)
      throws ClienteNoExistsException {
    return clienteService.buscarClientePorDni(dni);
  }

  @Operation(summary = "Crear Cliente")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description =
                "Información no válida\t\n"
                    + "Cliente ya existe\t\n"
                    + "Cliente menor de edad\t\n"
                    + "Cliente inactivo"),
      })
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ClienteResponseDto> crearCliente(
      @Valid @RequestBody @Parameter(name = "Cliente", description = "Datos del Cliente")
          ClienteRequestDto clienteDto)
      throws ClienteAlreadyExistsException,
          ClienteMenorDeEdadException,
          WrongInputDataException,
          ClienteInactivoException {
    clienteControllerValidator.validate(clienteDto);
    ClienteResponseDto clienteResponse = clienteService.darDeAltaCliente(clienteDto);
    return new ResponseEntity<ClienteResponseDto>(
        clienteResponse, new HttpHeaders(), HttpStatus.CREATED);
  }

  @Operation(
      summary = "Inactivar Cliente",
      description = "Inactivar un Cliente lo inhabilita de efectuar operaciones")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cliente inactivado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cliente inactivo"),
        @ApiResponse(responseCode = "404", description = "Cliente no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
      })
  @DeleteMapping(value = "/{dni}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ClienteResponseDto eliminarCliente(
      @PathVariable @Parameter(description = "DNI del Cliente", example = "12345678") long dni)
      throws CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          ClienteInactivoException {
    return clienteService.eliminarCliente(dni);
  }

  @Operation(summary = "Actualizar Cliente")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Información no válida\t\nCliente inactivo\t\nCliente menor de edad"),
        @ApiResponse(responseCode = "404", description = "Cliente no existe"),
      })
  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ClienteResponseDto actualizarCliente(
      @Valid @RequestBody @Parameter(name = "Cliente", description = "Datos del Cliente")
          ClienteRequestDto clienteDto)
      throws ClienteNoExistsException,
          ClienteMenorDeEdadException,
          WrongInputDataException,
          ClienteInactivoException {
    clienteControllerValidator.validate(clienteDto);
    return clienteService.actualizarCliente(clienteDto);
  }

  @Operation(
      summary = "Activar Cliente",
      description = "Activar un Cliente que ha sido desactivado/eliminado previamente")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cliente activado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cliente activo"),
        @ApiResponse(responseCode = "404", description = "Cliente no existe"),
      })
  @PatchMapping(value = "/{dni}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ClienteResponseDto activarCliente(
      @PathVariable @Parameter(description = "DNI del Cliente", example = "12345678") long dni)
      throws ClienteNoExistsException, ClienteActivoException {
    return clienteService.activarCliente(dni);
  }

  @Operation(summary = "Obtener todas las cuentas de un Cliente")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cuentas del Cliente"),
        @ApiResponse(responseCode = "400", description = "Cliente inactivo"),
        @ApiResponse(responseCode = "404", description = "Cliente no existe"),
      })
  @GetMapping(value = "/{dni}/cuentas", produces = MediaType.APPLICATION_JSON_VALUE)
  public ClienteCuentasResponseDto obtenerCuentasEnCliente(
      @PathVariable @Parameter(description = "DNI del Cliente", example = "12345678") long dni)
      throws ClienteNoExistsException, ClienteInactivoException {
    return clienteService.buscarCuentasDeClientePorDni(dni);
  }
}
