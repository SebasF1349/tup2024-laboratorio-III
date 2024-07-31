package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteCuentasResponseDto;
import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.controller.CuentaResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.validator.ClienteServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

  ClienteDao clienteDao;
  CuentaService cuentaService;

  @Autowired ClienteServiceValidator clienteServiceValidator;

  public ClienteService(ClienteDao clienteDao) {
    this.clienteDao = clienteDao;
  }

  @Autowired
  public void setCuentaService(@Lazy CuentaService cuentaService) {
    this.cuentaService = cuentaService;
  }

  public ClienteDto darDeAltaCliente(ClienteDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {

    Cliente cliente = new Cliente(clienteDto);

    clienteServiceValidator.validateClienteNoExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);

    return cliente.toClienteDto();
  }

  protected Cliente agregarCuenta(Cuenta cuenta, long dniTitular)
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {

    Cliente titular = buscarClienteCompletoPorDni(dniTitular);

    clienteServiceValidator.validateTipoCuentaUnica(titular, cuenta);

    cuenta.setTitular(titular);

    titular.addCuenta(cuenta);
    clienteDao.save(titular);
    return titular;
  }

  public ClienteDto buscarClientePorDni(long dni) throws ClienteNoExistsException {
    Cliente cliente = clienteDao.find(dni, false);

    if (cliente == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + dni);
    }

    return cliente.toClienteDto();
  }

  public ClienteDto actualizarCliente(ClienteDto clienteDto)
      throws ClienteNoExistsException, ClienteMenorDeEdadException {

    Cliente cliente = new Cliente(clienteDto);

    clienteServiceValidator.validateClienteExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);

    return cliente.toClienteDto();
  }

  public ClienteDto eliminarCliente(long dni)
      throws CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    Cliente cliente = buscarClienteCompletoPorDni(dni);

    cliente.setActivo(false);

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

    clienteDao.save(cliente);
    return cliente.toClienteDto();
  }

  public ClienteDto activarCliente(long dni) throws ClienteNoExistsException {
    Cliente cliente = buscarClienteCompletoPorDni(dni);

    cliente.setActivo(true);

    clienteDao.save(cliente);

    return cliente.toClienteDto();
  }

  protected void actualizarCliente(Cliente cliente)
      throws ClienteNoExistsException, ClienteMenorDeEdadException {

    clienteServiceValidator.validateClienteExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);
  }

  protected Cliente buscarClienteCompletoPorDni(long dni) throws ClienteNoExistsException {
    Cliente cliente = clienteDao.find(dni, true);

    if (cliente == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + dni);
    }

    return cliente;
  }

  protected Cliente getClienteByCuenta(long numeroCuenta) throws ClienteNoExistsException {
    Cliente cliente = clienteDao.getClienteByCuenta(numeroCuenta);

    if (cliente == null) {
      throw new ClienteNoExistsException("No existe un cliente con la cuenta solicitada.");
    }

    return cliente;
  }

  public ClienteCuentasResponseDto buscarCuentasDeClientePorDni(long dni)
      throws ClienteNoExistsException {
    Cliente cliente = buscarClienteCompletoPorDni(dni);

    ClienteCuentasResponseDto clienteCuentasResponseDto = cliente.toClienteCuentasResponseDto();

    for (Cuenta cuenta : cliente.getCuentas()) {
      CuentaResponseDto cuentaResponseDto = cuenta.toCuentaResponseDto();
      clienteCuentasResponseDto.addCuenta(cuentaResponseDto);
    }

    return clienteCuentasResponseDto;
  }
}
