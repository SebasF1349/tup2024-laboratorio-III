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

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.controller.CuentaMovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.MovimientoResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
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
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(titular);

    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(cuentaServiceValidator)
        .validateClienteHasntCuenta(any(Cuenta.class), eq(titular));

    assertThrows(
        TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteNoExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    CuentaDto cuentaDto = createCuentaDto();

    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .buscarClienteCompletoPorDni(cuentaDto.getTitular());

    assertThrows(ClienteNoExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteNoExistsCorruptedDataInDbException()
      throws ClienteNoExistsException,
          TipoCuentaAlreadyExistsException,
          ClienteMenorDeEdadException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(titular);

    doThrow(ClienteNoExistsException.class).when(clienteService).actualizarCliente(titular);

    assertThrows(CorruptedDataInDbException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaClienteMenorDeEdadCorruptedDataInDbException()
      throws ClienteNoExistsException,
          TipoCuentaAlreadyExistsException,
          ClienteMenorDeEdadException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(titular);

    doThrow(ClienteMenorDeEdadException.class).when(clienteService).actualizarCliente(titular);

    assertThrows(CorruptedDataInDbException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
  }

  @Test
  public void testDarDeAltaCuentaSuccess()
      throws CuentaNoSoportadaException,
          TipoCuentaAlreadyExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException,
          CorruptedDataInDbException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente titular = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(titular);

    CuentaDto cuentaDtoResult = cuentaService.darDeAltaCuenta(cuentaDto);

    assertEquals(cuentaDto.getBalance(), cuentaDtoResult.getBalance());
    assertEquals(TipoCuenta.CAJA_AHORROS.toString(), cuentaDtoResult.getTipoCuenta());
    assertEquals(TipoMoneda.PESOS_ARGENTINOS.toString(), cuentaDtoResult.getMoneda());
    assertEquals(cuentaDto.getTitular(), cuentaDtoResult.getTitular());

    verify(cuentaDao, times(1)).save(any(Cuenta.class));
  }

  @Test
  public void testBuscarCuentaPorIdCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException, IllegalArgumentException {
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
      throws ClienteNoExistsException, ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, false)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testBuscarCuentaPorIdSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    cuentaDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaDto.setMoneda(cuenta.getMoneda().toString());

    when(cuentaDao.find(numeroCuenta, false)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    assertEquals(cuentaDto, cuentaService.buscarCuentaPorId(numeroCuenta));
  }

  @Test
  public void testActualizarCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException, IllegalArgumentException {
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

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular()))
        .thenThrow(ClienteNoExistsException.class);

    assertThrows(ClienteNoExistsException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
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
  public void testActualizarCuentaNoExistsInClienteCorruptedDataInDbException()
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          TipoCuentaAlreadyExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(cliente);

    doThrow(ClienteNoExistsException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(CorruptedDataInDbException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaClienteMenorDeEdadCorruptedDataInDbException()
      throws ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          ClienteMenorDeEdadException,
          TipoCuentaAlreadyExistsException {
    CuentaDto cuentaDto = createCuentaDto();
    Cliente cliente = createCliente();

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(cliente);

    doThrow(ClienteMenorDeEdadException.class).when(clienteService).actualizarCliente(cliente);

    assertThrows(CorruptedDataInDbException.class, () -> cuentaService.actualizarCuenta(cuentaDto));
  }

  @Test
  public void testActualizarCuentaSuccess()
      throws CuentaNoExistsException,
          ClienteNoExistsException,
          CuentaNoExistsInClienteException,
          CuentaNoSoportadaException,
          ClienteMenorDeEdadException,
          TipoCuentaAlreadyExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          IllegalArgumentException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    cuentaDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaDto.setMoneda(cuenta.getMoneda().toString());

    when(clienteService.buscarClienteCompletoPorDni(cuentaDto.getTitular())).thenReturn(cliente);

    CuentaDto cuentaResult = cuentaService.actualizarCuenta(cuentaDto);
    assertEquals(cuenta.getBalance(), cuentaResult.getBalance());
    assertEquals(cuenta.getTipoCuenta().toString(), cuentaResult.getTipoCuenta());
    assertEquals(cuenta.getMoneda().toString(), cuentaResult.getMoneda());
    assertEquals(cuenta.getTitular().getDni(), cuentaResult.getTitular());

    verify(cuentaDao, times(1)).save(any(Cuenta.class));
  }

  @Test
  public void testEliminarCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.eliminarCuenta(numeroCuenta));
  }

  @Test
  public void testEliminarCuentaCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> cuentaService.eliminarCuenta(numeroCuenta));
  }

  @Test
  public void testEliminarCuentaSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    Cuenta cuenta = createCuenta();
    cuenta.setNumeroCuenta(123);
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    CuentaDto cuentaDto = createCuentaDto();
    cuentaDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaDto.setMoneda(cuenta.getMoneda().toString());

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    CuentaDto cuentaResDto = cuentaService.eliminarCuenta(numeroCuenta);

    assertEquals(cuentaDto, cuentaResDto);
    assertEquals(false, cuenta.isActivo());
    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testActivarCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException, IllegalArgumentException {

    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(CuentaNoExistsException.class)
        .when(cuentaServiceValidator)
        .validateCuentaExists(cuenta);

    assertThrows(CuentaNoExistsException.class, () -> cuentaService.activarCuenta(numeroCuenta));
  }

  @Test
  public void testActivarCuentaCorruptedDataInDbException()
      throws ClienteNoExistsException, ImpossibleException, IllegalArgumentException {
    Cuenta cuenta = createCuenta();

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    doThrow(ClienteNoExistsException.class)
        .when(clienteService)
        .getClienteByCuenta(cuenta.getNumeroCuenta());

    assertThrows(CorruptedDataInDbException.class, () -> cuentaService.activarCuenta(numeroCuenta));
  }

  @Test
  public void testActivarCuentaSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    Cuenta cuenta = createCuenta();
    cuenta.setNumeroCuenta(123);
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    CuentaDto cuentaDto = createCuentaDto();
    cuentaDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaDto.setMoneda(cuenta.getMoneda().toString());

    when(cuentaDao.find(numeroCuenta, true)).thenReturn(cuenta);
    when(clienteService.getClienteByCuenta(cuenta.getNumeroCuenta())).thenReturn(cliente);

    CuentaDto cuentaResDto = cuentaService.activarCuenta(numeroCuenta);

    assertEquals(cuentaDto, cuentaResDto);
    assertEquals(true, cuenta.isActivo());
    verify(cuentaDao, times(1)).save(cuenta);
  }

  @Test
  public void testBuscarCuentaCompletaPorIdCuentaNoExistsException()
      throws CuentaNoExistsException, ImpossibleException, IllegalArgumentException {
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
      throws ClienteNoExistsException, ImpossibleException, IllegalArgumentException {
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
  public void testBuscarCuentaCompletaPorIdSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    CuentaDto cuentaDto = createCuentaDto();
    Cuenta cuenta = createCuenta();
    Cliente cliente = createCliente();
    cuenta.setTitular(cliente);
    cuentaDto.setTipoCuenta(cuenta.getTipoCuenta().toString());
    cuentaDto.setMoneda(cuenta.getMoneda().toString());

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
      throws CuentaNoExistsException, ImpossibleException, IllegalArgumentException {
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
      throws ClienteNoExistsException, ImpossibleException, IllegalArgumentException {
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
  public void testBuscarTransaccionesDeCuentaSuccess()
      throws CuentaNoExistsException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    Cuenta cuenta = createCuenta();
    cuenta.setNumeroCuenta(123);
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

  private ClienteDto createClienteDto() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setDni(12345678);
    clienteDto.setNombre("Nombre");
    clienteDto.setApellido("Apellido");
    clienteDto.setFechaNacimiento("1990-01-01");
    clienteDto.setTipoPersona("F");
    clienteDto.setBanco("");
    return clienteDto;
  }

  private Cliente createCliente() {
    ClienteDto clienteDto = createClienteDto();
    return new Cliente(clienteDto);
  }

  private Movimiento createDeposito() {
    return new Deposito(500, createCuenta());
  }

  private Transferencia createTransferencia() {
    return new Transferencia(1000, createCuenta(), createCuenta());
  }
}
