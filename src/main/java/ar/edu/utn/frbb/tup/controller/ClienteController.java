package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ClienteControllerValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.ClienteService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

  @Autowired private ClienteService clienteService;
  @Autowired private CuentaService cuentaService;

  @Autowired private ClienteControllerValidator clienteControllerValidator;

  @GetMapping(value = "/{dni}")
  public Cliente obtenerCliente(@PathVariable long dni)
      throws ClienteNoExistsException, WrongInputDataException {
    return clienteService.buscarClientePorDni(dni);
  }

  @PostMapping
  public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody ClienteDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, WrongInputDataException {
    clienteControllerValidator.validate(clienteDto);
    Cliente clienteResponse = clienteService.darDeAltaCliente(clienteDto);
    return new ResponseEntity<Cliente>(clienteResponse, new HttpHeaders(), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{dni}")
  public Cliente eliminarCliente(@PathVariable long dni)
      throws ClienteNoExistsException, WrongInputDataException {
    Cliente cliente = clienteService.eliminarCliente(dni);
    for (Cuenta cuenta : cliente.getCuentas()) {
      try {
        cuentaService.eliminarCuenta(cuenta.getNumeroCuenta());
      } catch (CuentaNoExistsException ex) {
        System.out.printf(
            "Cuenta "
                + cuenta.getNumeroCuenta()
                + " de Cliente con DNI "
                + cliente.getDni()
                + " no existe");
      }
    }
    return cliente;
  }

  @PutMapping
  public Cliente actualizarCliente(@Valid @RequestBody ClienteDto clienteDto)
      throws ClienteNoExistsException, ClienteMenorDeEdadException, WrongInputDataException {
    clienteControllerValidator.validate(clienteDto);
    return clienteService.actualizarCliente(clienteDto);
  }

  @PatchMapping(value = "/{dni}")
  public Cliente activarCliente(@PathVariable long dni)
      throws ClienteNoExistsException, WrongInputDataException {
    Cliente cliente = clienteService.activarCliente(dni);
    for (Cuenta cuenta : cliente.getCuentas()) {
      try {
        cuentaService.activarCuenta(cuenta.getNumeroCuenta());
      } catch (CuentaNoExistsException ex) {
        System.out.printf(
            "Cuenta "
                + cuenta.getNumeroCuenta()
                + " de Cliente con DNI "
                + cliente.getDni()
                + " no existe");
      }
    }
    return cliente;
  }
}
