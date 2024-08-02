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
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
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

  public CuentaResponseDto darDeAltaCuenta(CuentaRequestDto cuentaRequestDto)
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CorruptedDataInDbException,
          ClienteInactivoException {

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
    return cuenta.toCuentaDto();
  }

  public CuentaResponseDto buscarCuentaPorId(long numeroCuenta)
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
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

  public CuentaResponseDto actualizarCuenta(@Valid CuentaRequestDto cuentaRequestDto)
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoSoportadaException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaNoExistsInClienteException {
    Cuenta cuenta = new Cuenta(cuentaRequestDto);

    cuentaServiceValidator.validateCuentaExists(cuenta);
    cuentaServiceValidator.validateTipoCuentaEstaSoportada(cuenta);

    Cliente titular = clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular());

    cuentaServiceValidator.validateClienteHasCuenta(cuenta, titular);

    try {
      clienteService.actualizarCliente(titular);
    } catch (ClienteNoExistsException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos con datos incorrectos");
    } catch (ClienteMenorDeEdadException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos con edad incorrecta");
    } catch (ClienteInactivoException ex) {
      throw new CorruptedDataInDbException(
          "Titular de cuenta guardado en Base de Datos inactivo, pero cuenta es activa ");
    }

    cuenta.setTitular(titular);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
  public CuentaResponseDto eliminarCuenta(long id)
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuenta.setActivo(false);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
  public CuentaResponseDto activarCuenta(long id)
    Cuenta cuenta = buscarCuentaCompletaPorId(id);

    cuenta.setActivo(true);
    cuentaDao.save(cuenta);
    return cuenta.toCuentaDto();
  }

  protected Cuenta buscarCuentaCompletaPorId(long numeroCuenta)
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
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

  protected void agregarMovimientoACuentas(MovimientoUnidireccional movimiento)
      throws ImpossibleException {
    Cuenta cuentaOrigen = movimiento.getCuenta();

    double nuevoSaldoOrigen = movimiento.actualizarCuentaMonto(cuentaOrigen);

    cuentaOrigen.setBalance(nuevoSaldoOrigen);
    cuentaOrigen.addMovimiento(movimiento);
  }

  protected void agregarTransferenciaACuentas(Transferencia transferencia) {
    Cuenta cuentaOrigen = transferencia.getCuenta();

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
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
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
