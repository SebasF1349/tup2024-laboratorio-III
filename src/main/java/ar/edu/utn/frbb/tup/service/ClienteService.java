package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

  ClienteDao clienteDao;

  public ClienteService(ClienteDao clienteDao) {
    this.clienteDao = clienteDao;
  }

  public Cliente darDeAltaCliente(ClienteDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {

    Cliente cliente = new Cliente(clienteDto);

    if (clienteDao.find(cliente.getDni(), false) != null) {
      throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
    }

    if (cliente.getEdad() < 18) {
      throw new ClienteMenorDeEdadException("El cliente debe ser mayor a 18 años");
    }

    clienteDao.save(cliente);
    return cliente;
  }

  public void agregarCuenta(Cuenta cuenta, String dniTitular)
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente titular = buscarClientePorDni(dniTitular);
    cuenta.setTitular(titular);
    if (titular.hasCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
      throw new TipoCuentaAlreadyExistsException(
          "El cliente ya posee una cuenta de ese tipo y moneda");
    }
    titular.addCuenta(cuenta);
    clienteDao.save(titular);
  }

  public Cliente buscarClientePorDni(String dni) throws ClienteNoExistsException {
    Cliente cliente = clienteDao.find(dni, true);

    if (cliente == null) {
      throw new ClienteNoExistsException("El cliente no existe");
    }

    return cliente;
  }

  public void actualizarCliente(Cliente cliente)
      throws ClienteNoExistsException, ClienteMenorDeEdadException {
    if (clienteDao.find(cliente.getDni(), false) == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + cliente.getDni());
    }

    if (cliente.getEdad() < 18) {
      throw new ClienteMenorDeEdadException("El cliente debe ser mayor a 18 años");
    }

    clienteDao.save(cliente);
  }

  public void eliminarCliente(Cliente cliente) throws ClienteNoExistsException {
    if (clienteDao.find(cliente.getDni(), false) == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + cliente.getDni());
    }
    cliente.setActivo(false);
    clienteDao.save(cliente);
  }
}
