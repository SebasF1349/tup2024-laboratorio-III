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
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
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
  public void validateCuentaNoExistsFail() throws ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta(), false)).thenReturn(cuenta);

    assertThrows(
        CuentaAlreadyExistsException.class,
        () -> cuentaServiceValidator.validateCuentaNoExists(cuenta));
  }

  @Test
  public void validateCuentaNoExistsSuccess() throws ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta(), false)).thenReturn(null);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateCuentaNoExists(cuenta));
  }

  @Test
  public void validateCuentaExistsFail() throws ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta(), false)).thenReturn(null);

    assertThrows(
        CuentaNoExistsException.class, () -> cuentaServiceValidator.validateCuentaExists(cuenta));
  }

  @Test
  public void validateCuentaExistsSuccess() throws ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta(), false)).thenReturn(cuenta);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateCuentaExists(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaPesosCaja() {
    Cuenta cuenta = createCuenta();
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);

    assertTrue(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaDolarCaja() {
    Cuenta cuenta = createCuenta();
    cuenta.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);

    assertTrue(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaPesosCorriente() {
    Cuenta cuenta = createCuenta();
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

    assertTrue(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  @Test
  public void tipoCuentaEstaSoportadaDolarCorriente() {
    Cuenta cuenta = createCuenta();
    cuenta.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

    assertFalse(cuentaServiceValidator.tipoCuentaEstaSoportada(cuenta));
  }

  private Cuenta createCuenta() {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1);
    cuenta.setBalance(500000);
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuenta.setActivo(true);
    return cuenta;
  }
}
