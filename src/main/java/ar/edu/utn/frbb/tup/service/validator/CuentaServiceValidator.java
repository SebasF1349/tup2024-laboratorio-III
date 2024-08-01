package ar.edu.utn.frbb.tup.service.validator;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CuentaServiceValidator {
  CuentaDao cuentaDao;

  private final Map<TipoCuenta, List<TipoMoneda>> cuentasSoportadas =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<TipoCuenta, List<TipoMoneda>>(
              TipoCuenta.CAJA_AHORROS,
              Arrays.asList(TipoMoneda.PESOS_ARGENTINOS, TipoMoneda.DOLARES_AMERICANOS)),
          new AbstractMap.SimpleEntry<TipoCuenta, List<TipoMoneda>>(
              TipoCuenta.CUENTA_CORRIENTE, Arrays.asList(TipoMoneda.PESOS_ARGENTINOS)));

  public CuentaServiceValidator(CuentaDao cuentaDao) {
    this.cuentaDao = cuentaDao;
  }

  public void validateCuentaNoExists(Cuenta cuenta)
      throws CuentaAlreadyExistsException, ImpossibleException {
    if (cuenta != null && cuentaDao.find(cuenta.getNumeroCuenta(), false) != null) {
      throw new CuentaAlreadyExistsException(
          "La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
    }
  }

  public void validateCuentaExists(Cuenta cuenta)
      throws CuentaNoExistsException, ImpossibleException {
    if (cuenta == null || cuentaDao.find(cuenta.getNumeroCuenta(), false) == null) {
      throw new CuentaNoExistsException("No existe una cuenta con el id suministrado.");
    }
  }

  public void validateTipoCuentaEstaSoportada(Cuenta cuenta) throws CuentaNoSoportadaException {
    if (!tipoCuentaEstaSoportada(cuenta)) {
      throw new CuentaNoSoportadaException(
          "El tipo de cuenta "
              + cuenta.getTipoCuenta()
              + " no soporta la moneda "
              + cuenta.getMoneda());
    }
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

  public void validateClienteHasCuenta(Cuenta cuenta, Cliente titular)
      throws CuentaNoExistsInClienteException {
    if (!titular.hasCuentaSameTipo(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
      throw new CuentaNoExistsInClienteException("El cliente no tiene la cuenta ingresada");
    }
  }

  public void validateClienteHasntCuenta(Cuenta cuenta, Cliente titular)
      throws TipoCuentaAlreadyExistsException {
    if (titular.hasCuentaSameTipo(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
      throw new TipoCuentaAlreadyExistsException("El cliente no tiene la cuenta ingresada");
    }
  }
}
