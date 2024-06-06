package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
  ClienteDao clienteDao = new ClienteDao();
  CuentaDao cuentaDao = new CuentaDao();

  public void darDeAltaCliente(Cliente cliente) throws ClienteAlreadyExistsException {
    if (clienteDao.find(cliente.getDni()) != null) {
      throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
    }

    if (cliente.getFechaNacimiento() == null) {
      throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
    }

    if (cliente.getEdad() < 18) {
      throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
    }

    clienteDao.save(cliente);
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
    Cliente cliente = clienteDao.find(dni);

    if (cliente == null) {
      throw new ClienteNoExistsException("El cliente no existe");
    }

    return cliente;
  }

  public void actualizarCliente(Cliente cliente) throws ClienteNoExistsException {
    if (clienteDao.find(cliente.getDni()) == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + cliente.getDni());
    }

    if (cliente.getFechaNacimiento() == null) {
      throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
    }

    if (cliente.getEdad() < 18) {
      throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
    }

    clienteDao.save(cliente);
  }

  public void eliminarCliente(Cliente cliente) {
    cliente.setActivo(false);
    clienteDao.save(cliente);
  }
}
