package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

  @Autowired private ClienteService clienteService;

  @Autowired private ClienteValidator clienteValidator;

  @GetMapping(value = "/{dni}")
  public Cliente obtenerCliente(@PathVariable String dni) throws ClienteNoExistsException {
    return clienteService.buscarClientePorDni(dni);
  }

  @PostMapping
  public Cliente crearCliente(@RequestBody ClienteDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, WrongInputDataException {
    clienteValidator.validate(clienteDto);
    return clienteService.darDeAltaCliente(clienteDto);
  }
}
