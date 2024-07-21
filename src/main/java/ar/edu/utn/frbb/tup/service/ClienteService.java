package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.validator.ClienteServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

  ClienteDao clienteDao;

  @Autowired ClienteServiceValidator clienteServiceValidator;

  public ClienteService(ClienteDao clienteDao) {
    this.clienteDao = clienteDao;
  }

  public Cliente darDeAltaCliente(ClienteDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {

    Cliente cliente = new Cliente(clienteDto);

    clienteServiceValidator.validateClienteNoExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);
    return cliente;
  }

  public Cliente agregarCuenta(Cuenta cuenta, long dniTitular)
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente titular = buscarClientePorDni(dniTitular);
    cuenta.setTitular(titular);

    clienteServiceValidator.validateTipoCuentaUnica(titular, cuenta);

    titular.addCuenta(cuenta);
    clienteDao.save(titular);
    return titular;
  }

  public Cliente obtenerTitularConCuenta(Cuenta cuenta, long dniTitular)
      throws ClienteNoExistsException, TipoCuentaAlreadyExistsException {
    Cliente titular = buscarClientePorDni(dniTitular);

    clienteServiceValidator.validateTipoCuentaUnica(titular, cuenta);

    Cuenta cuentaOriginal = titular.getCuenta(cuenta.getNumeroCuenta());

    if (cuentaOriginal != null) {
      titular.deleteCuenta(cuentaOriginal);
    }

    titular.addCuenta(cuenta);
    return titular;
  }

  public Cliente buscarClientePorDni(long dni) throws ClienteNoExistsException {
    Cliente cliente = clienteDao.find(dni, true);

    if (cliente == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + dni);
    }

    return cliente;
  }

  public Cliente actualizarCliente(ClienteDto clienteDto)
      throws ClienteNoExistsException, ClienteMenorDeEdadException {

    Cliente cliente = new Cliente(clienteDto);

    clienteServiceValidator.validateClienteExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);

    return cliente;
  }

  public void actualizarCliente(Cliente cliente)
      throws ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException {

    clienteServiceValidator.validateClienteExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);
  }

  public Cliente eliminarCliente(long dni) throws ClienteNoExistsException {
    Cliente cliente = buscarClientePorDni(dni);

    cliente.setActivo(false);

    clienteDao.save(cliente);
    return cliente;
  }

  public Cliente activarCliente(long dni) throws ClienteNoExistsException {
    Cliente cliente = buscarClientePorDni(dni);

    cliente.setActivo(true);
    clienteDao.save(cliente);
    return cliente;
  }
}
