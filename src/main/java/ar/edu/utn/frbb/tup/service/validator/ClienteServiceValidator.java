package ar.edu.utn.frbb.tup.service.validator;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceValidator {
  ClienteDao clienteDao;

  public ClienteServiceValidator(ClienteDao clienteDao) {
    this.clienteDao = clienteDao;
  }

  public void validateClienteNoExists(Cliente cliente) throws ClienteAlreadyExistsException {
    if (clienteDao.find(cliente.getDni(), false) != null) {
      throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
    }
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
      throws TipoCuentaAlreadyExistsException {
    if (titular.hasCuentaSameTipo(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
      throw new TipoCuentaAlreadyExistsException(
          "El cliente ya posee una cuenta de ese tipo y moneda");
    }
  }
}
