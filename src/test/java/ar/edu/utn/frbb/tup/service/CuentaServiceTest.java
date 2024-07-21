package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
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
  public void testDarDeAltaCuentaNoSoportadaException() throws CuentaNoSoportadaException {
    CuentaDto cuentaDto = createCuentaDto();

    doThrow(CuentaNoSoportadaException.class)
        .when(cuentaServiceValidator)
        .validateTipoCuentaEstaSoportada(any(Cuenta.class));

    assertThrows(CuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaTipoCuentaAlreadyExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();

    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(clienteService)
        .obtenerTitularConCuenta(any(Cuenta.class), eq(clienteDni));

    assertThrows(
        TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteNoExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();

    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .obtenerTitularConCuenta(any(Cuenta.class), eq(clienteDni));

    assertThrows(ClienteNoExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteMenorDeEdadException()
      throws ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException,
          TipoCuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente cliente = new Cliente();

    when(clienteService.obtenerTitularConCuenta(any(Cuenta.class), eq(cuentaDto.getTitular())))
        .thenReturn(cliente);

    doThrow(ClienteMenorDeEdadException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(ClienteMenorDeEdadException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaSuccess()
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException {
    CuentaDto cuentaDto = createCuentaDto();
    // Cuenta cuenta = new Cuenta(cuentaDto);
    Cliente cliente = new Cliente();

    when(clienteService.obtenerTitularConCuenta(any(Cuenta.class), eq(cuentaDto.getTitular())))
        .thenReturn(cliente);

    CuentaDto cuentaDtoResult = cuentaService.darDeAltaCuenta(cuentaDto);

    assertEquals(cuentaDto.getBalance(), cuentaDtoResult.getBalance());
    assertEquals(cuentaDto.getTipoCuenta(), cuentaDtoResult.getTipoCuenta());
    assertEquals(cuentaDto.getMoneda(), cuentaDtoResult.getMoneda());
    assertEquals(cuentaDto.getTitular(), cuentaDtoResult.getTitular());

    // verify(cuentaDao, times(1)).save(cuenta);
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

    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(any(Cuenta.class));

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaClienteNoExistsException()
      throws ClienteNoExistsException, TipoCuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();

    when(clienteService.obtenerTitularConCuenta(any(Cuenta.class), eq(cuentaDto.getTitular())))
        .thenThrow(ClienteNoExistsException.class);

    assertThrows(ClienteNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaNoExistsInClienteException()
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          TipoCuentaAlreadyExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente cliente = new Cliente();

    when(clienteService.obtenerTitularConCuenta(any(Cuenta.class), eq(cuentaDto.getTitular())))
        .thenReturn(cliente);

    doThrow(CuentaNoExistsInClienteException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(
        CuentaNoExistsInClienteException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaNoSoportadaException() throws CuentaNoSoportadaException {
    CuentaDto cuentaDto = createCuentaDto();

    doThrow(CuentaNoSoportadaException.class)
        .when(cuentaServiceValidator)
        .validateTipoCuentaEstaSoportada(any(Cuenta.class));

    assertThrows(CuentaNoSoportadaException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaClienteMenorDeEdadException()
      throws ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException,
          TipoCuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente cliente = new Cliente();

    when(clienteService.obtenerTitularConCuenta(any(Cuenta.class), eq(cuentaDto.getTitular())))
        .thenReturn(cliente);

    doThrow(ClienteMenorDeEdadException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(
        ClienteMenorDeEdadException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaTipoCuentaAlreadyExistsException()
      throws ClienteNoExistsException, TipoCuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();

    when(clienteService.obtenerTitularConCuenta(any(Cuenta.class), eq(cuentaDto.getTitular())))
        .thenThrow(TipoCuentaAlreadyExistsException.class);

    assertThrows(
        TipoCuentaAlreadyExistsException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaSuccess()
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          CuentaNoSoportadaException,
          ClienteMenorDeEdadException,
          TipoCuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = new Cuenta(cuentaDto);

    Cuenta cuentaResult = cuentaService.actualizarCuenta(cuentaDto);
    assertEquals(cuenta.getNumeroCuenta(), cuentaResult.getNumeroCuenta());
    assertEquals(cuenta.getBalance(), cuentaResult.getBalance());
    assertEquals(cuenta.getTipoCuenta(), cuentaResult.getTipoCuenta());
    assertEquals(cuenta.getMoneda(), cuentaResult.getMoneda());
    assertEquals(cuenta.getTitular(), cuentaResult.getTitular());

    verify(cuentaDao, times(1)).save(cuentaResult);
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
