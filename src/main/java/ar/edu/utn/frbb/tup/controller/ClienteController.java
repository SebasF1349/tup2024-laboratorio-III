package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ClienteControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.ClienteService;
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
@RequestMapping("/api/cliente")
public class ClienteController {

  @Autowired private ClienteService clienteService;

  @Autowired private ClienteControllerValidator clienteControllerValidator;

  @GetMapping(value = "/{dni}")
  public ClienteDto obtenerCliente(@PathVariable long dni)
      throws ClienteNoExistsException, WrongInputDataException {
    return clienteService.buscarClientePorDni(dni);
  }

  @PostMapping
  public ResponseEntity<ClienteDto> crearCliente(@Valid @RequestBody ClienteDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, WrongInputDataException {
    clienteControllerValidator.validate(clienteDto);
    ClienteDto clienteResponse = clienteService.darDeAltaCliente(clienteDto);
    return new ResponseEntity<ClienteDto>(clienteResponse, new HttpHeaders(), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{dni}")
  public ClienteDto eliminarCliente(@PathVariable long dni)
      throws CorruptedDataInDbException, ClienteNoExistsException {
    return clienteService.eliminarCliente(dni);
  }

  @PutMapping
  public ClienteDto actualizarCliente(@Valid @RequestBody ClienteDto clienteDto)
      throws ClienteNoExistsException, ClienteMenorDeEdadException, WrongInputDataException {
    clienteControllerValidator.validate(clienteDto);
    return clienteService.actualizarCliente(clienteDto);
  }

  @PatchMapping(value = "/{dni}")
  public ClienteDto activarCliente(@PathVariable long dni) throws ClienteNoExistsException {
    return clienteService.activarCliente(dni);
  }
}
