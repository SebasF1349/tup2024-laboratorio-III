package ar.edu.utn.frbb.tup.service.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceValidatorTest {
  @Mock private CuentaDao cuentaDao;

  @InjectMocks private CuentaServiceValidator cuentaServiceValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void validateCuentaNoExistsFail() {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(cuenta);

    assertThrows(
        CuentaAlreadyExistsException.class,
        () -> cuentaServiceValidator.validateCuentaNoExists(cuenta));
  }

  @Test
  public void validateCuentaNoExistsSuccess() {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(null);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateCuentaNoExists(cuenta));
  }

  @Test
  public void validateCuentaExistsFail() {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(null);

    assertThrows(
        CuentaNoExistsException.class, () -> cuentaServiceValidator.validateCuentaExists(cuenta));
  }

  @Test
  public void validateCuentaExistsSuccess() {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(cuenta);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateCuentaExists(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaPesosCaja() {
    Cuenta cuenta =
        new Cuenta()
            .setMoneda(TipoMoneda.PESOS_ARGENTINOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORROS);

    assertTrue(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaDolarCaja() {
    Cuenta cuenta =
        new Cuenta()
            .setMoneda(TipoMoneda.DOLARES_AMERICANOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORROS);

    assertTrue(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaPesosCorriente() {
    Cuenta cuenta =
        new Cuenta()
            .setMoneda(TipoMoneda.PESOS_ARGENTINOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

    assertTrue(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaDolarCorriente() {
    Cuenta cuenta =
        new Cuenta()
            .setMoneda(TipoMoneda.DOLARES_AMERICANOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

    assertFalse(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  private Cuenta createCuenta() {
    return new Cuenta()
        .setMoneda(TipoMoneda.PESOS_ARGENTINOS)
        .setBalance(500000)
        .setTipoCuenta(TipoCuenta.CAJA_AHORROS);
  }
}
