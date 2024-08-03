package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteCuentasResponseDto;
import ar.edu.utn.frbb.tup.controller.ClienteRequestDto;
import ar.edu.utn.frbb.tup.controller.ClienteResponseDto;
import ar.edu.utn.frbb.tup.controller.CuentaResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteActivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaInactivaException;
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

  public ClienteResponseDto darDeAltaCliente(ClienteRequestDto clienteDto)
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, ClienteInactivoException {

    Cliente cliente = new Cliente(clienteDto);

    clienteServiceValidator.validateClienteNoExists(cliente);
    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);

    return cliente.toClienteDto();
  }

  protected Cliente agregarCuenta(Cuenta cuenta, long dniTitular)
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException, CuentaInactivaException {

    Cliente titular = buscarClienteCompletoPorDni(dniTitular);

    clienteServiceValidator.validateTipoCuentaUnica(titular, cuenta);

    cuenta.setTitular(titular);

    titular.addCuenta(cuenta);
    clienteDao.save(titular);
    return titular;
  }

  public ClienteResponseDto buscarClientePorDni(long dni) throws ClienteNoExistsException {
    Cliente cliente = clienteDao.find(dni, false);

    if (cliente == null) {
      throw new ClienteNoExistsException("No existe un cliente con DNI " + dni);
    }

    return cliente.toClienteDto();
  }

  public ClienteResponseDto actualizarCliente(ClienteRequestDto clienteRequestDto)
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    Cliente cliente = clienteDao.find(clienteRequestDto.getDni(), false);

    if (cliente == null) {
      throw new ClienteNoExistsException(
          "No existe un cliente con DNI " + clienteRequestDto.getDni());
    }

    clienteServiceValidator.validateClienteIsActivo(cliente);

    cliente.actualizarConRequestDto(clienteRequestDto);

    clienteServiceValidator.validateClienteMayorDeEdad(cliente);

    clienteDao.save(cliente);

    return cliente.toClienteDto();
  }

  public ClienteResponseDto eliminarCliente(long dni)
      throws CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          ClienteInactivoException {
    Cliente cliente = buscarClienteCompletoPorDni(dni);

    clienteServiceValidator.validateClienteIsActivo(cliente);

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
      } catch (CuentaInactivaException e) {
        System.out.printf(
            "Cuenta "
                + cuenta.getNumeroCuenta()
                + " de Cliente con DNI "
                + cliente.getDni()
                + "ya estaba inactiva");
      }
    }

    clienteDao.save(cliente);
    return cliente.toClienteDto();
  }

  public ClienteResponseDto activarCliente(long dni)
      throws ClienteNoExistsException, ClienteActivoException {
    Cliente cliente = buscarClienteCompletoPorDni(dni);

    clienteServiceValidator.validateClienteIsNotActivo(cliente);

    cliente.setActivo(true);

    clienteDao.save(cliente);

    return cliente.toClienteDto();
  }

  protected void actualizarCliente(Cliente cliente)
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {

    clienteServiceValidator.validateClienteExists(cliente);
    clienteServiceValidator.validateClienteIsActivo(cliente);
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
      throws ClienteNoExistsException, ClienteInactivoException {
    Cliente cliente = buscarClienteCompletoPorDni(dni);

    clienteServiceValidator.validateClienteIsActivo(cliente);

    ClienteCuentasResponseDto clienteCuentasResponseDto = cliente.toClienteCuentasResponseDto();

    for (Cuenta cuenta : cliente.getCuentas()) {
      CuentaResponseDto cuentaResponseDto = cuenta.toCuentaResponseDto();
      cuentaResponseDto.setTitular(cliente.getDni());
      clienteCuentasResponseDto.addCuenta(cuentaResponseDto);
    }

    return clienteCuentasResponseDto;
  }
}
