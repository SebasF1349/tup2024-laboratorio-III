package ar.edu.utn.frbb.tup.service.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaActivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaInactivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
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

  @Test
  public void validateClienteHasCuentaDifferentMonedaCuentaNoExistsInClienteException() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuentaInCliente.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertThrows(
        CuentaNoExistsInClienteException.class,
        () -> cuentaServiceValidator.validateClienteHasCuenta(cuenta, cliente));
  }

  @Test
  public void validateClienteHasCuentaDiferentTipoCuentaNoExistsInClienteException() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuentaInCliente.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertThrows(
        CuentaNoExistsInClienteException.class,
        () -> cuentaServiceValidator.validateClienteHasCuenta(cuenta, cliente));
  }

  @Test
  public void validateClienteHasCuentaInactivaException() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuentaInCliente.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuentaInCliente.setActivo(false);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertThrows(
        CuentaInactivaException.class,
        () -> cuentaServiceValidator.validateClienteHasCuenta(cuenta, cliente));
  }

  @Test
  public void validateClienteHasCuentaSuccess() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuentaInCliente.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateClienteHasCuenta(cuenta, cliente));
  }

  @Test
  public void validateClienteHasntCuentaTipoCuentaAlreadyExistsException() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuentaInCliente.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> cuentaServiceValidator.validateClienteHasntCuenta(cuenta, cliente));
  }

  @Test
  public void validateClienteHasntCuentaInactivaException() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuentaInCliente.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuentaInCliente.setActivo(false);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertThrows(
        CuentaInactivaException.class,
        () -> cuentaServiceValidator.validateClienteHasntCuenta(cuenta, cliente));
  }

  @Test
  public void validateClienteHasntCuentaSuccess() {
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaInCliente = createCuenta();
    cuentaInCliente.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuentaInCliente.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cliente cliente = createCliente();
    cliente.addCuenta(cuentaInCliente);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateClienteHasntCuenta(cuenta, cliente));
  }

  @Test
  public void validateCuentaIsActivaInactivaException() {
    Cuenta cuenta = createCuenta();
    cuenta.setActivo(false);

    assertThrows(
        CuentaInactivaException.class, () -> cuentaServiceValidator.validateCuentaIsActiva(cuenta));
  }

  @Test
  public void validateCuentaIsActivaSuccess() {
    Cuenta cuenta = createCuenta();
    cuenta.setActivo(true);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateCuentaIsActiva(cuenta));
  }

  @Test
  public void validateCuentaIsNotActivaActivaException() {
    Cuenta cuenta = createCuenta();
    cuenta.setActivo(true);

    assertThrows(
        CuentaActivaException.class,
        () -> cuentaServiceValidator.validateCuentaIsNotActiva(cuenta));
  }

  @Test
  public void validateCuentaIsNotActivaSuccess() {
    Cuenta cuenta = createCuenta();
    cuenta.setActivo(false);

    assertDoesNotThrow(() -> cuentaServiceValidator.validateCuentaIsNotActiva(cuenta));
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

  private Cliente createCliente() {
    Cliente cliente = new Cliente();
    cliente.setDni(123456);
    cliente.setNombre("Nombre");
    cliente.setApellido("Apellido");
    cliente.setActivo(true);
    return cliente;
  }
}
