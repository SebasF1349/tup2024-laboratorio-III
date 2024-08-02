package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.controller.ClienteRequestDto;
import ar.edu.utn.frbb.tup.controller.CuentaMovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.CuentaRequestDto;
import ar.edu.utn.frbb.tup.controller.CuentaResponseDto;
import ar.edu.utn.frbb.tup.controller.MovimientoResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
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
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();

    doThrow(CuentaNoSoportadaException.class)
        .when(cuentaServiceValidator)
        .validateTipoCuentaEstaSoportada(any(Cuenta.class));

    assertThrows(
        CuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaTipoCuentaAlreadyExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException, CuentaInactivaException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(titular);

    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(cuentaServiceValidator)
        .validateClienteHasntCuenta(any(Cuenta.class), eq(titular));

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteNoExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();

    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .buscarClienteCompletoPorDni(cuentaRequestDto.getTitular());

    assertThrows(
        ClienteNoExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteNoExistsCorruptedDataInDbException()
      throws ClienteNoExistsException,
          TipoCuentaAlreadyExistsException,
          ClienteMenorDeEdadException,
          ClienteInactivoException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(titular);

    doThrow(ClienteNoExistsException.class).when(clienteService).actualizarCliente(titular);

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteMenorDeEdadCorruptedDataInDbException()
      throws ClienteNoExistsException,
          TipoCuentaAlreadyExistsException,
          ClienteMenorDeEdadException,
          ClienteInactivoException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(titular);

    doThrow(ClienteMenorDeEdadException.class).when(clienteService).actualizarCliente(titular);

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteInactivoExceptionException()
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(titular);

    doThrow(ClienteInactivoException.class).when(clienteService).actualizarCliente(titular);

    assertThrows(
        ClienteInactivoException.class, () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaInactivaException()
      throws ClienteNoExistsException, TipoCuentaAlreadyExistsException, CuentaInactivaException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(titular);

    doThrow(CuentaInactivaException.class)
        .when(cuentaServiceValidator)
        .validateClienteHasntCuenta(any(Cuenta.class), eq(titular));

    assertThrows(
        CuentaInactivaException.class, () -> cuentaService.darDeAltaCuenta(cuentaRequestDto));
  }

  @Test
  public void testDarDeAltaCuentaSuccess()
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException,
          ClienteInactivoException,
          CorruptedDataInDbException,
          CuentaInactivaException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(titular);

    CuentaResponseDto cuentaResponseDto = cuentaService.darDeAltaCuenta(cuentaRequestDto);

    assertEquals(cuentaRequestDto.getBalance(), cuentaResponseDto.getBalance());
    assertEquals(TipoCuenta.CAJA_AHORROS.toString(), cuentaResponseDto.getTipoCuenta());
    assertEquals(TipoMoneda.PESOS_ARGENTINOS.toString(), cuentaResponseDto.getMoneda());
    assertEquals(cuentaRequestDto.getTitular(), cuentaResponseDto.getTitular());

    verify(cuentaDao, times(1)).save(any(Cuenta.class));
  }

  @Test
  public void testBuscarCuentaPorIdCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, false)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(
        CuentaNoExistsException.class, () -> cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaPorIdCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, false)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaPorIdFindImpossibleException() throws ImpossibleException {

    doThrow(ImpossibleException.class).when(cuentaDao).find(numeroCuenta, false);

    assertThrows(ImpossibleException.class, () -> cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaPorIdValidateImpossibleException()
      throws ImpossibleException, CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, false)).thenReturn(cuenta);

    doThrow(ImpossibleException.class).when(cuentaServiceValidator).validateCuentaExists(cuenta);

    assertThrows(ImpossibleException.class, () -> cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaPorIdSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException {
    Cuenta cuenta = createCuenta();
    cuenta.setActivo(true);
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    CuentaResponseDto cuentaResponseDto = createCuentaResponseDto();
    cuentaResponseDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaResponseDto.setMoneda(cuenta.getMoneda().toString());
    cuentaResponseDto.setActivo(true);

    when(cuentaDao.find(numeroCuenta, false)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    assertEquals(cuentaResponseDto, cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testActualizarCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();

    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(any(Cuenta.class));

    assertThrows(
        CuentaNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaClienteNoExistsException() throws ClienteNoExistsException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenThrow(ClienteNoExistsException.class);

    assertThrows(
        ClienteNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaNoSoportadaException() throws CuentaNoSoportadaException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();

    doThrow(CuentaNoSoportadaException.class)
        .when(cuentaServiceValidator)
        .validateTipoCuentaEstaSoportada(any(Cuenta.class));

    assertThrows(
        CuentaNoSoportadaException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaNoExistsInClienteCorruptedDataInDbException()
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(cliente);

    doThrow(ClienteNoExistsException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaClienteMenorDeEdadCorruptedDataInDbException()
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(cliente);

    doThrow(ClienteMenorDeEdadException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaClienteClienteInactivoExceptionCorruptedDataInDbException()
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(cliente);

    doThrow(ClienteInactivoException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaImpossibleException()
      throws CuentaNoExistsException, ImpossibleException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();

    doThrow(ImpossibleException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(any(Cuenta.class));

    assertThrows(ImpossibleException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaNoExistsInClienteException()
      throws CuentaNoExistsInClienteException, ClienteNoExistsException, CuentaInactivaException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(cliente);

    doThrow(CuentaNoExistsInClienteException.class)
        .when(cuentaServiceValidator)
        .validateClienteHasCuenta(any(Cuenta.class), eq(cliente));

    assertThrows(
        CuentaNoExistsInClienteException.class,
        () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaInactivaException()
      throws CuentaNoExistsInClienteException, ClienteNoExistsException, CuentaInactivaException {
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(cliente);

    doThrow(CuentaInactivaException.class)
        .when(cuentaServiceValidator)
        .validateClienteHasCuenta(any(Cuenta.class), eq(cliente));

    assertThrows(
        CuentaInactivaException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDto));
  }

  @Test
  public void testActualizarCuentaSuccess()
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          CuentaNoSoportadaException,
          CorruptedDataInDbException,
          ImpossibleException,
          CuentaInactivaException {
    Cuenta cuenta = createCuenta();
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    CuentaRequestDto cuentaRequestDto = createCuentaRequestDto();
    cuentaRequestDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaRequestDto.setMoneda(cuenta.getMoneda().toString());

    when(clienteService.buscarClienteCompletoPorDni(cuentaRequestDto.getTitular()))
        .thenReturn(cliente);

    CuentaResponseDto cuentaResponseDto = cuentaService.actualizarCuenta(cuentaRequestDto);
    assertEquals(cuenta.getBalance(), cuentaResponseDto.getBalance());
    assertEquals(cuenta.getTipoCuenta().toString(), cuentaResponseDto.getTipoCuenta());
    assertEquals(cuenta.getMoneda().toString(), cuentaResponseDto.getMoneda());
    assertEquals(cuenta.getTitular().getDni(), cuentaResponseDto.getTitular());

    verify(cuentaDao, times(1)).save(any(Cuenta.class));
  }

  @Test
  public void testEliminarCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.eliminarCuenta(numeroCuenta));
  }

  @Test
  public void testEliminarCuentaCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.eliminarCuenta(numeroCuenta));
  }

  @Test
  public void testEliminarCuentaCuentaDaoImpossibleException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    doThrow(ImpossibleException.class).when(cuentaDao).find(cuenta.getNumeroCuenta(), true);

    assertThrows(ImpossibleException.class, () -> cuentaService.eliminarCuenta(numeroCuenta));
  }

  @Test
  public void testEliminarCuentaInactivaException()
      throws ImpossibleException, CuentaInactivaException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);

    doThrow(CuentaInactivaException.class)
        .when(cuentaServiceValidator)
        .validateCuentaIsActiva(cuenta);

    assertThrows(CuentaInactivaException.class, () -> cuentaService.eliminarCuenta(numeroCuenta));
  }

  @Test
  public void testEliminarCuentaSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          CuentaInactivaException {
    Cuenta cuenta = createCuenta();
    cuenta.setNumeroCuenta(123);
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    CuentaResponseDto cuentaResponseDtoExpected = createCuentaResponseDto();
    cuentaResponseDtoExpected.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaResponseDtoExpected.setMoneda(cuenta.getMoneda().toString());
    cuentaResponseDtoExpected.setActivo(false);

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    CuentaResponseDto cuentaResponseDto = cuentaService.eliminarCuenta(numeroCuenta);

    assertEquals(cuentaResponseDtoExpected, cuentaResponseDto);
    assertEquals(false, cuenta.isActivo());
    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testActivarCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException {

    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.activarCuenta(numeroCuenta));
  }

  @Test
  public void testActivarCuentaCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(CorruptedDataInDbException.class, () -> cuentaService.activarCuenta(numeroCuenta));
  }

  @Test
  public void testActivarCuentaCuentaDaoImpossibleException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    doThrow(ImpossibleException.class).when(cuentaDao).find(cuenta.getNumeroCuenta(), true);

    assertThrows(ImpossibleException.class, () -> cuentaService.activarCuenta(numeroCuenta));
  }

  @Test
  public void testActivarCuentaActivaException() throws ImpossibleException, CuentaActivaException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);

    doThrow(CuentaActivaException.class)
        .when(cuentaServiceValidator)
        .validateCuentaIsNotActiva(cuenta);

    assertThrows(CuentaActivaException.class, () -> cuentaService.activarCuenta(numeroCuenta));
  }

  @Test
  public void testActivarCuentaSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          CuentaActivaException {
    Cuenta cuenta = createCuenta();
    cuenta.setNumeroCuenta(123);
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    CuentaResponseDto cuentaResponseDtoExpected = createCuentaResponseDto();
    cuentaResponseDtoExpected.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaResponseDtoExpected.setMoneda(cuenta.getMoneda().toString());
    cuentaResponseDtoExpected.setActivo(true);

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    CuentaResponseDto cuentaResponseDto = cuentaService.activarCuenta(numeroCuenta);

    assertEquals(cuentaResponseDtoExpected, cuentaResponseDto);
    assertEquals(true, cuenta.isActivo());
    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testBuscarCuentaCompletaPorIdCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(
        CuentaNoExistsException.class, () -> cuentaService.buscarCuentaCompletaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaCompletaPorIdCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class,
        () -> cuentaService.buscarCuentaCompletaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaCompletaPorIdImpossibleException()
      throws CuentaNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ImpossibleException.class).when(cuentaServiceValidator).validateCuentaExists(cuenta);

    assertThrows(
        ImpossibleException.class, () -> cuentaService.buscarCuentaCompletaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarTransaccionesDeCuentaPorIdCuentaInactivaException()
      throws ImpossibleException, CuentaInactivaException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);

    doThrow(CuentaInactivaException.class)
        .when(cuentaServiceValidator)
        .validateCuentaIsActiva(cuenta);

    assertThrows(
        CuentaInactivaException.class,
        () -> cuentaService.buscarTransaccionesDeCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaCompletaPorIdSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException {
    Cuenta cuenta = createCuenta();
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    assertEquals(cuenta, cuentaService.buscarCuentaCompletaPorId(numeroCuenta));
  }

  @Test
  public void testAgregarMovimientoACuentasRetiroSuccess() throws ImpossibleException {
    double balance = 1500;
    double monto = 1000;
    Cuenta cuenta = createCuenta();
    cuenta.setBalance(balance);
    Retiro retiro = new Retiro(monto, cuenta);
    retiro.setMovimientoId(123);

    cuentaService.agregarMovimientoACuentas(retiro);

    assertEquals(cuenta.getBalance(), balance - monto);
    assertEquals(cuenta.getMovimiento(retiro.getMovimientoId()), retiro);
  }

  @Test
  public void testAgregarMovimientoACuentasDepositoSuccess() throws ImpossibleException {
    double balance = 1500;
    double monto = 1000;
    Cuenta cuenta = createCuenta();
    cuenta.setBalance(balance);
    Deposito deposito = new Deposito(monto, cuenta);
    deposito.setMovimientoId(123);

    cuentaService.agregarMovimientoACuentas(deposito);

    assertEquals(cuenta.getBalance(), balance + monto);
    assertEquals(cuenta.getMovimiento(deposito.getMovimientoId()), deposito);
  }

  @Test
  public void testAgregarTransferenciaACuentasDistintoBancoSuccess() {
    double balance = 1500;
    double monto = 1000;
    Cuenta cuentaOrigen = createCuenta();
    cuentaOrigen.setBalance(balance);

    Transferencia transferencia = new Transferencia(monto, null, cuentaOrigen);
    transferencia.setMovimientoId(123);
    transferencia.setMontoDebitado(monto);

    cuentaService.agregarTransferenciaACuentas(transferencia);

    assertEquals(transferencia.getMontoDebitado(), monto);
    assertEquals(cuentaOrigen.getBalance(), balance - monto);
    assertEquals(cuentaOrigen.getMovimiento(transferencia.getMovimientoId()), transferencia);
    assertNull(transferencia.getCuentaDestino());
  }

  @Test
  public void testAgregarTransferenciaACuentasMismoBancoSuccess() {
    double balance1 = 1500;
    double balance2 = 2000;
    double monto = 1000;
    Cuenta cuentaOrigen = createCuenta();
    cuentaOrigen.setBalance(balance1);
    Cuenta cuentaDestino = createCuenta();
    cuentaDestino.setBalance(balance2);
    Transferencia transferencia = new Transferencia(monto, cuentaDestino, cuentaOrigen);
    transferencia.setMovimientoId(123);
    transferencia.setMontoDebitado(monto);

    cuentaService.agregarTransferenciaACuentas(transferencia);

    assertEquals(transferencia.getMontoDebitado(), monto);
    assertEquals(cuentaOrigen.getBalance(), balance1 - monto);
    assertEquals(cuentaOrigen.getMovimiento(transferencia.getMovimientoId()), transferencia);
    assertEquals(cuentaDestino.getBalance(), balance2 + monto);
    assertEquals(cuentaDestino.getMovimiento(transferencia.getMovimientoId()), transferencia);
  }

  @Test
  public void testBuscarTransaccionesDeCuentaNoExistsException()
      throws ImpossibleException, CuentaNoExistsException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(
        CuentaNoExistsException.class,
        () -> cuentaService.buscarTransaccionesDeCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarTransaccionesDeCuentaCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class,
        () -> cuentaService.buscarTransaccionesDeCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarTransaccionesDeCuentaImpossibleException()
      throws ClienteNoExistsException, ImpossibleException {
    Cuenta cuenta = createCuenta();

    doThrow(ImpossibleException.class).when(cuentaDao).find(cuenta.getNumeroCuenta(), true);

    assertThrows(
        ImpossibleException.class,
        () -> cuentaService.buscarTransaccionesDeCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarTransaccionesDeCuentaSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          CuentaInactivaException {
    Cuenta cuenta = createCuenta();
    Movimiento movimiento = createDeposito();
    cuenta.addMovimiento(movimiento);
    Transferencia transferencia = createTransferencia();
    cuenta.addMovimiento(transferencia);

    CuentaMovimientosResponseDto cuentaMovimientosResponseDtoExpected =
        cuenta.toCuentaMovimientoResponseDto();
    MovimientoResponseDto movimientoResponseDto =
        movimiento.toMovimientoResponseDto(cuenta.getNumeroCuenta());
    cuentaMovimientosResponseDtoExpected.addMovimiento(movimientoResponseDto);
    MovimientoResponseDto transferenciaResponseDto =
        transferencia.toMovimientoResponseDto(cuenta.getNumeroCuenta());
    cuentaMovimientosResponseDtoExpected.addMovimiento(transferenciaResponseDto);

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);

    CuentaMovimientosResponseDto cuentaMovimientosResponseDto =
        cuentaService.buscarTransaccionesDeCuentaPorId(numeroCuenta);

    assertEquals(cuentaMovimientosResponseDtoExpected, cuentaMovimientosResponseDto);
  }

  private CuentaRequestDto createCuentaRequestDto() {
    CuentaRequestDto cuentaRequestDto = new CuentaRequestDto();
    cuentaRequestDto.setBalance(500000);
    cuentaRequestDto.setMoneda("P");
    cuentaRequestDto.setTipoCuenta("A");
    cuentaRequestDto.setTitular(clienteDni);
    return cuentaRequestDto;
  }

  private CuentaResponseDto createCuentaResponseDto() {
    CuentaResponseDto cuentaResponseDto = new CuentaResponseDto();
    cuentaResponseDto.setBalance(500000);
    cuentaResponseDto.setMoneda("P");
    cuentaResponseDto.setTipoCuenta("A");
    cuentaResponseDto.setTitular(clienteDni);
    cuentaResponseDto.setActivo(true);
    return cuentaResponseDto;
  }

  private Cuenta createCuenta() {
    Cuenta cuenta = new Cuenta(createCuentaRequestDto());
    cuenta.setNumeroCuenta(numeroCuenta);
    return cuenta;
  }

  private ClienteRequestDto createClienteDto() {
    ClienteRequestDto clienteRequestDto = new ClienteRequestDto();
    clienteRequestDto.setDni(12345678);
    clienteRequestDto.setNombre("Nombre");
    clienteRequestDto.setApellido("Apellido");
    clienteRequestDto.setFechaNacimiento("1990-01-01");
    clienteRequestDto.setTipoPersona("F");
    clienteRequestDto.setBanco("");
    return clienteRequestDto;
  }

  private Cliente createCliente() {
    ClienteRequestDto clienteRequestDto = createClienteDto();
    return new Cliente(clienteRequestDto);
  }

  private Movimiento createDeposito() {
    return new Deposito(500, createCuenta());
  }

  private Transferencia createTransferencia() {
    return new Transferencia(1000, createCuenta(), createCuenta());
  }
}
