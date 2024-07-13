package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
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
public class CuentaServiceTest {

  @Mock private CuentaDao cuentaDao;
  @Mock private ClienteService clienteService;
  private final String clienteDni = "12345678";

  @InjectMocks private CuentaService cuentaService;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testDarDeAltaCuentaExistenteException() {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(cuenta);

    assertThrows(
        CuentaAlreadyExistsException.class,
        () -> cuentaService.darDeAltaCuenta(cuenta, clienteDni));
  }

  @Test
  public void testDarDeAltaCuentaNoSoportadaException() {
    Cuenta cuenta = createCuenta(TipoMoneda.DOLARES_AMERICANOS, TipoCuenta.CUENTA_CORRIENTE);

    assertThrows(
        CuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuenta, clienteDni));
  }

  @Test
  public void testDarDeAltaCuentaDuplicadaException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cuenta cuenta = createCuenta();

    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(clienteService)
        .agregarCuenta(cuenta, clienteDni);

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> cuentaService.darDeAltaCuenta(cuenta, clienteDni));
  }

  @Test
  public void testDarDeAltaCuentaSuccess()
      throws TipoCuentaAlreadyExistsException,
          CuentaNoSoportadaException,
          CuentaAlreadyExistsException,
          ClienteNoExistsException {
    Cuenta cuenta = createCuenta();

    cuentaService.darDeAltaCuenta(cuenta, clienteDni);

    verify(cuentaDao, times(1)).find(cuenta.getNumeroCuenta());
    assertTrue(cuentaService.tipoCuentaEstaSoportada(cuenta));
    verify(clienteService, times(1)).agregarCuenta(cuenta, clienteDni);
    verify(cuentaDao, times(1)).save(cuenta);
  }

  private Cuenta createCuenta() {
    return new Cuenta()
        .setMoneda(TipoMoneda.PESOS_ARGENTINOS)
        .setBalance(500000)
        .setTipoCuenta(TipoCuenta.CAJA_AHORROS);
  }

  private Cuenta createCuenta(TipoMoneda moneda, TipoCuenta cuenta) {
    return new Cuenta().setMoneda(moneda).setBalance(500000).setTipoCuenta(cuenta);
  }
}
