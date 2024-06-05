package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {
  CuentaDao cuentaDao = new CuentaDao();
  MovimientoDao movimientoDao = new MovimientoDao();

  public void darDeAltaCuenta(Cuenta cuenta) throws CuentaAlreadyExistsException {
    if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
      throw new CuentaAlreadyExistsException(
          "La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
    }
    cuentaDao.save(cuenta);
  }

  public Cuenta find(String numeroCuenta) {
    return cuentaDao.find(numeroCuenta);
  }

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
