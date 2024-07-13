package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {
  CuentaDao cuentaDao = new CuentaDao();
  MovimientoDao movimientoDao = new MovimientoDao();

  private final Map<TipoCuenta, List<TipoMoneda>> cuentasSoportadas =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<TipoCuenta, List<TipoMoneda>>(
              TipoCuenta.CAJA_AHORROS,
              Arrays.asList(TipoMoneda.PESOS_ARGENTINOS, TipoMoneda.DOLARES_AMERICANOS)),
          new AbstractMap.SimpleEntry<TipoCuenta, List<TipoMoneda>>(
              TipoCuenta.CUENTA_CORRIENTE, Arrays.asList(TipoMoneda.PESOS_ARGENTINOS)));

  @Autowired ClienteService clienteService;

  public void darDeAltaCuenta(Cuenta cuenta, String dniTitular)
      throws CuentaAlreadyExistsException,
          CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException {

    if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
      throw new CuentaAlreadyExistsException(
          "La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
    }

    if (!tipoCuentaEstaSoportada(cuenta)) {
      throw new CuentaNoSoportadaException(
          "El tipo de cuenta "
              + cuenta.getTipoCuenta()
              + " no soporta la moneda "
              + cuenta.getMoneda());
    }

    clienteService.agregarCuenta(cuenta, dniTitular);
    cuentaDao.save(cuenta);
  }

  public Cuenta find(String numeroCuenta) {
    return cuentaDao.find(numeroCuenta);
  }

  public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
    TipoMoneda moneda = cuenta.getMoneda();
    TipoCuenta tipoCuenta = cuenta.getTipoCuenta();
    if (!cuentasSoportadas.containsKey(tipoCuenta)) {
      return false;
    }
    if (!cuentasSoportadas.get(tipoCuenta).contains(moneda)) {
      return false;
    }
    return true;
  }

  // NOTE: missing tests
  public void agregarMovimiento(Cuenta cuenta, Movimiento movimiento) {
    double nuevoSaldo = movimiento.actualizarCuentaMonto(cuenta.getBalance());
    if (nuevoSaldo < 0) {
      throw new IllegalArgumentException("Saldo insuficiente");
    }
    cuenta.setBalance(nuevoSaldo);
    cuenta.addMovimiento(movimiento);
    movimientoDao.save(movimiento);
  }
}
