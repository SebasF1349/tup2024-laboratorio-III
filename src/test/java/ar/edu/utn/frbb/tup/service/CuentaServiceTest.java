package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsInClienteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.validator.CuentaServiceValidator;
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
  @Mock private CuentaServiceValidator cuentaServiceValidator;
  @Mock private ClienteService clienteService;
  private final long numeroCuenta = 1;
  private final long clienteDni = 12345678;

  @InjectMocks private CuentaService cuentaService;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testDarDeAltaCuentaAlreadExistsException() throws CuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();

    doThrow(CuentaAlreadyExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaNoExists(cuenta);

    assertThrows(
        CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaNoSoportadaException() throws CuentaNoSoportadaException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();

    doThrow(CuentaNoSoportadaException.class)
        .when(cuentaServiceValidator)
        .validateTipoCuentaEstaSoportada(cuenta);

    assertThrows(CuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaTipoCuentaAlreadyExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();

    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(clienteService)
        .agregarCuenta(cuenta, clienteDni);

    assertThrows(
        TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteNoExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();

    doThrow(ClienteNoExistsException.class).when(clienteService).agregarCuenta(cuenta, clienteDni);

    assertThrows(ClienteNoExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaSuccess()
      throws TipoCuentaAlreadyExistsException,
          CuentaNoSoportadaException,
          CuentaAlreadyExistsException,
          ClienteNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();

    assertEquals(cuenta, cuentaService.darDeAltaCuenta(cuentaDto));
    verify(clienteService, times(1)).agregarCuenta(cuenta, clienteDni);
    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testBuscarCuentaPorIdClienteNoExistsException() throws CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(
        CuentaNoExistsException.class, () -> cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaPorIdSuccess() throws CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta)).thenReturn(cuenta);

    assertEquals(cuenta, cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testActualizarCuentaNoExistsException() throws CuentaNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = new Cuenta(cuentaDto);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaClienteNoExistsException()
      throws ClienteNoExistsException, CuentaNoExistsInClienteException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = new Cuenta(cuentaDto);

    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .actualizarCuenta(cuenta, cuentaDto.getTitular());

    assertThrows(ClienteNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaNoExistsInClienteException()
      throws ClienteNoExistsException, CuentaNoExistsInClienteException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = new Cuenta(cuentaDto);

    doThrow(CuentaNoExistsInClienteException.class)
        .when(clienteService)
        .actualizarCuenta(cuenta, cuentaDto.getTitular());

    assertThrows(
        CuentaNoExistsInClienteException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaSuccess()
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          CuentaNoSoportadaException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = new Cuenta(cuentaDto);

    assertEquals(cuenta, cuentaService.actualizarCuenta(cuentaDto));

    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testEliminarCuentaNoExists() throws CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(clienteDni)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.eliminarCuenta(clienteDni));
  }

  @Test
  public void testEliminarCuentaSuccess() throws CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(clienteDni)).thenReturn(cuenta);

    cuentaService.eliminarCuenta(clienteDni);

    assertEquals(false, cuenta.isActivo());
    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testActivarCuentaNoExistsException() throws CuentaNoExistsException {

    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(clienteDni)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.activarCuenta(clienteDni));
  }

  @Test
  public void testActivarCuentaSuccess() throws CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(clienteDni)).thenReturn(cuenta);

    cuentaService.activarCuenta(clienteDni);

    assertEquals(true, cuenta.isActivo());
    verify(cuentaDao, times(1)).save(cuenta);
  }

  private CuentaDto createCuentaDto() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setBalance(500000);
    cuentaDto.setMoneda("P");
    cuentaDto.setTipoCuenta("A");
    cuentaDto.setTitular(clienteDni);
    return cuentaDto;
  }

  private Cuenta createCuenta() {
    return new Cuenta(createCuentaDto());
  }
}
