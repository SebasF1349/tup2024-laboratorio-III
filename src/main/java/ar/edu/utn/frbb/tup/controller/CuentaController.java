package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

  @Autowired private CuentaService cuentaService;

  @Autowired private CuentaControllerValidator cuentaValidator;

  @GetMapping(value = "/{id}")
  public Cuenta obtenerCuenta(@PathVariable long id) throws CuentaNoExistsException {
    return cuentaService.buscarCuentaPorId(id);
  }

  @PostMapping
  public ResponseEntity<Cuenta> crearCuenta(@Valid @RequestBody CuentaDto cuentaDto)
      throws WrongInputDataException,
          CuentaAlreadyExistsException,
          CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException {
    cuentaValidator.validate(cuentaDto);
    Cuenta cuentaResponse = cuentaService.darDeAltaCuenta(cuentaDto);
    return new ResponseEntity<Cuenta>(cuentaResponse, new HttpHeaders(), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{id}")
  public Cuenta eliminarCuenta(@PathVariable long id) throws CuentaNoExistsException {
    return cuentaService.eliminarCuenta(id);
  }

  @PutMapping
  public Cuenta actualizarCuenta(@Valid @RequestBody CuentaDto cuentaDto)
      throws WrongInputDataException,
          CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          CuentaNoSoportadaException {
    cuentaValidator.validate(cuentaDto);
    return cuentaService.actualizarCuenta(cuentaDto);
  }
}
