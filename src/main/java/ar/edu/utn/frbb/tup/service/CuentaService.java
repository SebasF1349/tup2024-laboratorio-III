package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.controller.CuentaMovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.MovimientoResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.MovimientoUnidireccional;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.validator.CuentaServiceValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {
  CuentaDao cuentaDao = new CuentaDao();
  MovimientoDao movimientoDao = new MovimientoDao();
  ClienteService clienteService;

  @Autowired CuentaServiceValidator cuentaServiceValidator;

  @Autowired
  public void setClienteService(@Lazy ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  public CuentaDto darDeAltaCuenta(CuentaDto cuentaDto)
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CorruptedDataInDbException {

    Cuenta cuenta = new Cuenta(cuentaDto);

    cuentaServiceValidator.validateTipoCuentaEstaSoportada(cuenta);

    Cliente titular = clienteService.obtenerTitularConCuenta(cuenta, cuentaDto.getTitular());

    cuenta.setTitular(titular);
    try {
      clienteService.actualizarCliente(titular);
    } catch (ClienteNoExistsException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos con datos incorrectos");
    } catch (ClienteMenorDeEdadException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos con edad incorrecta");
    }
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  public CuentaDto buscarCuentaPorId(long numeroCuenta)
      throws CuentaNoExistsException, CorruptedDataInDbException {
    Cuenta cuenta = cuentaDao.find(numeroCuenta, false);

    cuentaServiceValidator.validateCuentaExists(cuenta);

    Cliente titular;
    try {
      titular = clienteService.getClienteByCuenta(cuenta.getNumeroCuenta());
    } catch (ClienteNoExistsException ex) {
      throw new CorruptedDataInDbException(
          "Cuenta guardada en Base de Datos con datos de Titular incorrectos");
    }
    cuenta.setTitular(titular);

    return cuenta.toCuentaDto();
  }

  public CuentaDto actualizarCuenta(@Valid CuentaDto cuentaDto)
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoSoportadaException,
          CorruptedDataInDbException,
          TipoCuentaAlreadyExistsException {
    Cuenta cuenta = new Cuenta(cuentaDto);

    cuentaServiceValidator.validateCuentaExists(cuenta);
    cuentaServiceValidator.validateTipoCuentaEstaSoportada(cuenta);

    Cliente titular = clienteService.obtenerTitularConCuenta(cuenta, cuentaDto.getTitular());

    try {
      clienteService.actualizarCliente(titular);
    } catch (ClienteNoExistsException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos con datos incorrectos");
    } catch (ClienteMenorDeEdadException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos con edad incorrecta");
    }

    cuenta.setTitular(titular);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  public CuentaDto eliminarCuenta(long id)
      throws CuentaNoExistsException, CorruptedDataInDbException {
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuenta.setActivo(false);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  public CuentaDto activarCuenta(long id)
      throws CuentaNoExistsException, CorruptedDataInDbException {
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuenta.setActivo(true);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  protected Cuenta buscarCuentaCompletaPorId(long numeroCuenta)
      throws CuentaNoExistsException, CorruptedDataInDbException {
    Cuenta cuenta = cuentaDao.find(numeroCuenta, true);

    cuentaServiceValidator.validateCuentaExists(cuenta);

    Cliente titular;
    try {
      titular = clienteService.getClienteByCuenta(cuenta.getNumeroCuenta());
    } catch (ClienteNoExistsException ex) {
      throw new CorruptedDataInDbException(
          "Cuenta guardada en Base de Datos con datos de Titular incorrectos");
    }
    cuenta.setTitular(titular);

    return cuenta;
  }

  protected void agregarMovimientoACuentas(MovimientoUnidireccional movimiento) {
    Cuenta cuentaOrigen = movimiento.getCuenta();

    double nuevoSaldoOrigen = movimiento.actualizarCuentaMonto(cuentaOrigen);
    // TODO: is validation needed if it doesn't interact with the client?
    cuentaOrigen.setBalance(nuevoSaldoOrigen);
    cuentaOrigen.addMovimiento(movimiento);
  }

  protected void agregarTransferenciaACuentas(Transferencia transferencia) {
    Cuenta cuentaOrigen = transferencia.getCuenta();

    // TODO: is validation needed if it doesn't interact with the client?
    cuentaOrigen.setBalance(transferencia.getNuevoMontoCuentaOrigen());
    cuentaOrigen.addMovimiento(transferencia);

    Cuenta cuentaDestino = transferencia.getCuentaDestino();

    if (cuentaDestino == null) {
      return;
    }

    cuentaDestino.setBalance(transferencia.getNuevoMontoCuentaDestino());
    cuentaDestino.addMovimiento(transferencia);
  }

  public CuentaMovimientosResponseDto buscarTransaccionesDeCuentaPorId(long id)
      throws CuentaNoExistsException, CorruptedDataInDbException {
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    CuentaMovimientosResponseDto cuentaMovimientosResponseDto =
        cuenta.toCuentaMovimientoResponseDto();

    for (Movimiento movimiento : cuenta.getMovimientos()) {
      MovimientoResponseDto movimientoResponseDto = movimiento.toMovimientoResponseDto(id);
      cuentaMovimientosResponseDto.addMovimiento(movimientoResponseDto);
    }

    return cuentaMovimientosResponseDto;
  }
}
