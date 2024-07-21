package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.validator.CuentaServiceValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {
  CuentaDao cuentaDao = new CuentaDao();
  MovimientoDao movimientoDao = new MovimientoDao();

  @Autowired ClienteService clienteService;
  @Autowired CuentaServiceValidator cuentaServiceValidator;

  public CuentaDto darDeAltaCuenta(CuentaDto cuentaDto)
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException {

    Cuenta cuenta = new Cuenta(cuentaDto);

    cuentaServiceValidator.validateTipoCuentaEstaSoportada(cuenta);

    Cliente titular = clienteService.obtenerTitularConCuenta(cuenta, cuentaDto.getTitular());

    cuenta.setTitular(titular);
    cuentaDao.save(cuenta);
    clienteService.actualizarCliente(titular);
    cuentaDto.setTitular(titular.getDni());
    return cuentaDto;
  }

  public Cuenta buscarCuentaPorId(long numeroCuenta) throws CuentaNoExistsException {
    Cuenta cuenta = cuentaDao.find(numeroCuenta);

    cuentaServiceValidator.validateCuentaExists(cuenta);

    return cuenta;
  }

  // TODO: missing tests
  public void agregarMovimiento(Cuenta cuenta, Movimiento movimiento) {
    double nuevoSaldo = movimiento.actualizarCuentaMonto(cuenta.getBalance());
    // TODO: move to validation?
    if (nuevoSaldo < 0) {
      throw new IllegalArgumentException("Saldo insuficiente");
    }
    cuenta.setBalance(nuevoSaldo);
    cuenta.addMovimiento(movimiento);
    movimientoDao.save(movimiento);
  }

  public Cuenta actualizarCuenta(@Valid CuentaDto cuentaDto)
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          CuentaNoSoportadaException,
          ClienteMenorDeEdadException,
          TipoCuentaAlreadyExistsException {
    Cuenta cuenta = new Cuenta(cuentaDto);

    cuentaServiceValidator.validateCuentaExists(cuenta);
    cuentaServiceValidator.validateTipoCuentaEstaSoportada(cuenta);

    Cliente titular = clienteService.obtenerTitularConCuenta(cuenta, cuentaDto.getTitular());
    clienteService.actualizarCliente(titular);
    cuentaDao.save(cuenta);
    return cuenta;
  }

  public Cuenta eliminarCuenta(long dni) throws CuentaNoExistsException {
    Cuenta cuenta = buscarCuentaPorId(dni);

    cuenta.setActivo(false);
    cuentaDao.save(cuenta);
    // TODO: Should set movimientos to inactive too
    return cuenta;
  }

  public Cuenta activarCuenta(long id) throws CuentaNoExistsException {
    Cuenta cuenta = buscarCuentaPorId(id);

    cuenta.setActivo(true);
    cuentaDao.save(cuenta);
    return cuenta;
  }
}
