package ar.edu.utn.frbb.tup.service.validator;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteActivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaInactivaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceValidator {
  ClienteDao clienteDao;

  public ClienteServiceValidator(ClienteDao clienteDao) {
    this.clienteDao = clienteDao;
  }

  public void validateClienteNoExists(Cliente cliente)
      throws ClienteAlreadyExistsException, ClienteInactivoException {
    Cliente possibleCliente = clienteDao.find(cliente.getDni(), false);
    if (possibleCliente == null) {
      return;
    }
    if (!possibleCliente.isActivo()) {
      throw new ClienteInactivoException(
          "Ya existe un cliente inactivo con DNI " + cliente.getDni());
    }
    throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
  }

  public void validateClienteExists(Cliente cliente) throws ClienteNoExistsException {
    if (clienteDao.find(cliente.getDni(), false) == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + cliente.getDni());
    }
  }

  public void validateClienteMayorDeEdad(Cliente cliente) throws ClienteMenorDeEdadException {
    if (cliente.getEdad() < 18) {
      throw new ClienteMenorDeEdadException("El cliente debe ser mayor a 18 años");
    }
  }

  public void validateTipoCuentaUnica(Cliente titular, Cuenta cuenta)
      throws TipoCuentaAlreadyExistsException, CuentaInactivaException {
    Cuenta possibleCuenta = titular.cuentaSameTipo(cuenta.getTipoCuenta(), cuenta.getMoneda());
    if (possibleCuenta == null) {
      return;
    }
    if (!possibleCuenta.isActivo()) {
      throw new CuentaInactivaException(
          "El cliente ya posee una cuenta del mismo tipo, pero está inhabilitada");
    }
    throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta del mismo tipo");
  }

  public void validateClienteIsActivo(Cliente cliente) throws ClienteInactivoException {
    if (!cliente.isActivo()) {
      throw new ClienteInactivoException("El cliente está inactivo");
    }
  }

  public void validateClienteIsNotActivo(Cliente cliente) throws ClienteActivoException {
    if (cliente.isActivo()) {
      throw new ClienteActivoException("El cliente está inactivo");
    }
  }
}
