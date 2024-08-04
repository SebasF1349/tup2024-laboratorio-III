package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaMovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.CuentaRequestDto;
import ar.edu.utn.frbb.tup.controller.CuentaResponseDto;
import ar.edu.utn.frbb.tup.controller.MovimientoResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.MovimientoUnidireccional;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaActivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaInactivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.validator.CuentaServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {
  CuentaDao cuentaDao;
  MovimientoDao movimientoDao;
  ClienteService clienteService;

  @Autowired CuentaServiceValidator cuentaServiceValidator;

  public CuentaService(CuentaDao cuentaDao) {
    this.cuentaDao = cuentaDao;
  }

  @Autowired
  public void setClienteService(@Lazy ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  public CuentaResponseDto darDeAltaCuenta(CuentaRequestDto cuentaRequestDto)
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CorruptedDataInDbException,
          ClienteInactivoException,
          CuentaInactivaException,
          ImpossibleException {

    Cuenta cuenta = new Cuenta(cuentaRequestDto);

    cuentaServiceValidator.validateTipoCuentaEstaSoportada(cuenta);

    Cliente titular = clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular());

    cuentaServiceValidator.validateClienteHasntCuenta(cuenta, titular);

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

    titular.addCuenta(cuenta);
    try {
      clienteService.actualizarCliente(titular);
    } catch (ClienteNoExistsException e) {
      throw new ImpossibleException();
    } catch (ClienteMenorDeEdadException e) {
      throw new ImpossibleException();
    } catch (ClienteInactivoException e) {
      throw new ImpossibleException();
    }

    return cuenta.toCuentaDto();
  }

  public CuentaResponseDto buscarCuentaPorId(long numeroCuenta)
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    Cuenta cuenta = cuentaDao.find(numeroCuenta, false);

    if (cuenta == null) {
      throw new CuentaNoExistsException("No existe una cuenta con el id " + numeroCuenta);
    }

    if (cuenta.isExterna()) {
      throw new CuentaNoExistsException("Cuenta solicitada es externa");
    }

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

  public CuentaResponseDto eliminarCuenta(long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuentaServiceValidator.validateCuentaIsActiva(cuenta);

    cuenta.setActivo(false);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  public CuentaResponseDto activarCuenta(long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaActivaException {
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuentaServiceValidator.validateCuentaIsNotActiva(cuenta);

    cuenta.setActivo(true);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  protected Cuenta buscarCuentaCompletaPorId(long numeroCuenta)
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    Cuenta cuenta = cuentaDao.find(numeroCuenta, true);

    if (cuenta == null) {
      throw new CuentaNoExistsException("No existe una cuenta con el id " + numeroCuenta);
    }

    if (cuenta.isExterna()) {
      throw new CuentaNoExistsException("Cuenta solicitada es externa");
    }

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

  protected void agregarMovimientoACuentas(MovimientoUnidireccional movimiento)
      throws ImpossibleException {
    Cuenta cuentaOrigen = movimiento.getCuenta();

    double nuevoSaldoOrigen = movimiento.actualizarCuentaMonto(cuentaOrigen);

    cuentaOrigen.setBalance(nuevoSaldoOrigen);
    cuentaOrigen.addMovimiento(movimiento);
    cuentaDao.save(cuentaOrigen);
  }

  protected void agregarTransferenciaACuentas(Transferencia transferencia) {
    Cuenta cuentaOrigen = transferencia.getCuenta();

    cuentaOrigen.setBalance(transferencia.getNuevoMontoCuentaOrigen());
    cuentaOrigen.addMovimiento(transferencia);

    cuentaDao.save(cuentaOrigen);

    Cuenta cuentaDestino = transferencia.getCuentaDestino();

    if (cuentaDestino == null) {
      return;
    }

    cuentaDestino.setBalance(transferencia.getNuevoMontoCuentaDestino());
    cuentaDestino.addMovimiento(transferencia);
    cuentaDao.save(cuentaDestino);
  }

  public CuentaMovimientosResponseDto buscarTransaccionesDeCuentaPorId(long id)
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuentaServiceValidator.validateCuentaIsActiva(cuenta);

    CuentaMovimientosResponseDto cuentaMovimientosResponseDto =
        cuenta.toCuentaMovimientoResponseDto();

    for (Movimiento movimiento : cuenta.getMovimientos()) {
      MovimientoResponseDto movimientoResponseDto = movimiento.toMovimientoResponseDto(id);
      cuentaMovimientosResponseDto.addMovimiento(movimientoResponseDto);
    }

    return cuentaMovimientosResponseDto;
  }
}
