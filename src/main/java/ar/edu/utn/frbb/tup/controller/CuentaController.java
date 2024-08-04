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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/cuenta")
public class CuentaController {

  @Autowired private CuentaService cuentaService;
  @Autowired private CuentaControllerValidator cuentaValidator;

  @GetMapping(value = "/{id}")
  public CuentaResponseDto obtenerCuenta(@PathVariable long id)
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    return cuentaService.buscarCuentaPorId(id);
  }

  @PostMapping
  public ResponseEntity<CuentaResponseDto> crearCuenta(
      @Valid @RequestBody CuentaRequestDto cuentaRequestDto)
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

  @DeleteMapping(value = "/{id}")
  public CuentaResponseDto eliminarCuenta(@PathVariable long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    return cuentaService.eliminarCuenta(id);
  }

  @PatchMapping(value = "/{id}")
  public CuentaResponseDto activarCuenta(@PathVariable long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaActivaException {
    return cuentaService.activarCuenta(id);
  }

  @GetMapping(value = "/{id}/transacciones")
  public CuentaMovimientosResponseDto obtenerTransaccionesEnCuenta(@PathVariable long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    return cuentaService.buscarTransaccionesDeCuentaPorId(id);
  }
}
