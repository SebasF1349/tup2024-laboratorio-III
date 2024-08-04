package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.controller.DepositoRequestDto;
import ar.edu.utn.frbb.tup.controller.DepositoResponseDto;
import ar.edu.utn.frbb.tup.controller.RetiroRequestDto;
import ar.edu.utn.frbb.tup.controller.RetiroResponseDto;
import ar.edu.utn.frbb.tup.controller.TransferenciaRequestDto;
import ar.edu.utn.frbb.tup.controller.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.externalService.Banelco;
import ar.edu.utn.frbb.tup.externalService.BanelcoResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.MovimientoUnidireccional;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.validator.MovimientoServiceValidator;
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
public class MovimientoServiceTest {

  @Mock private MovimientoDao movimientoDao;
  @Mock private MovimientoServiceValidator movimientoServiceValidator;
  @Mock private CuentaService cuentaService;
  @Mock private Banelco banelco;

  @InjectMocks private MovimientoService movimientoService;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testRealizarTransferenciaCuentaNoExistsException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen());

    assertThrows(
        CuentaNoExistsException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaMontoInsuficienteException()
      throws CuentaNoExistsException,
          MontoInsuficienteException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(MontoInsuficienteException.class)
        .when(movimientoServiceValidator)
        .validateMonto(any(Transferencia.class));

    assertThrows(
        MontoInsuficienteException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaMonedasDistintasAMonedaIngresadaException()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(MonedasDistintasException.class)
        .when(movimientoServiceValidator)
        .validateMonedaIngresadaCorrecta(cuentaOrigen, transferenciaDto);

    assertThrows(
        MonedasDistintasException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaMonedasDistintasBetweenCuentasException()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(MonedasDistintasException.class)
        .when(movimientoServiceValidator)
        .validateSameMonedaBetweenCuentas(any(Transferencia.class));

    assertThrows(
        MonedasDistintasException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaCuentaOrigenCorruptedDataInDbException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();

    doThrow(CorruptedDataInDbException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen());

    assertThrows(
        CorruptedDataInDbException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaCuentaDestinoCorruptedDataInDbException()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(CorruptedDataInDbException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino());

    assertThrows(
        CorruptedDataInDbException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaBanelcoMonedasDistintasException()
      throws CuentaNoExistsException,
          MonedasDistintasException,
          BanelcoErrorException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);
    BanelcoResponseDto banelcoResponse = createBanelcoResponse();

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino());

    when(banelco.transferir(
            cuentaOrigen, transferenciaDto.getCuentaDestino(), transferenciaDto.getMonto()))
        .thenReturn(banelcoResponse);

    doThrow(BanelcoErrorException.class)
        .when(movimientoServiceValidator)
        .validateBanelcoResponse(banelcoResponse);

    assertThrows(
        BanelcoErrorException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaBanelcoCuentaNoExistsException()
      throws CuentaNoExistsException,
          MonedasDistintasException,
          BanelcoErrorException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);
    BanelcoResponseDto banelcoResponse = createBanelcoResponse();

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino());

    when(banelco.transferir(
            cuentaOrigen, transferenciaDto.getCuentaDestino(), transferenciaDto.getMonto()))
        .thenReturn(banelcoResponse);

    doThrow(CuentaNoExistsException.class)
        .when(movimientoServiceValidator)
        .validateBanelcoResponse(banelcoResponse);

    assertThrows(
        CuentaNoExistsException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaBanelcoErrorException()
      throws CuentaNoExistsException,
          MonedasDistintasException,
          BanelcoErrorException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);
    BanelcoResponseDto banelcoResponse = createBanelcoResponse();

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino());

    when(banelco.transferir(
            cuentaOrigen, transferenciaDto.getCuentaDestino(), transferenciaDto.getMonto()))
        .thenReturn(banelcoResponse);

    doThrow(MonedasDistintasException.class)
        .when(movimientoServiceValidator)
        .validateBanelcoResponse(banelcoResponse);

    assertThrows(
        MonedasDistintasException.class,
        () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaImpossibleException()
      throws CuentaNoExistsException,
          MonedasDistintasException,
          BanelcoErrorException,
          CorruptedDataInDbException,
          ImpossibleException {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();

    doThrow(ImpossibleException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen());

    assertThrows(
        ImpossibleException.class, () -> movimientoService.realizarTransferencia(transferenciaDto));
  }

  @Test
  public void testRealizarTransferenciaMismoBancoSuccess()
      throws CuentaNoExistsException,
          MontoInsuficienteException,
          MonedasDistintasException,
          BanelcoErrorException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException {

    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);
    Cuenta cuentaDestino = createCuenta(2);
    TransferenciaResponseDto transferenciaResponseDto = createTransferenciaResponseDto();
    transferenciaResponseDto.setMoneda(cuentaOrigen.getMoneda().toString());

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino()))
        .thenReturn(cuentaDestino);

    TransferenciaResponseDto transferenciaDtoResult =
        movimientoService.realizarTransferencia(transferenciaDto);

    verify(movimientoDao, times(1)).save(any(Transferencia.class));
    assertEquals(
        transferenciaResponseDto.getMovimientoId(), transferenciaDtoResult.getMovimientoId());
    assertEquals(
        transferenciaResponseDto.getCuentaOrigen(), transferenciaDtoResult.getCuentaOrigen());
    assertEquals(
        transferenciaResponseDto.getCuentaDestino(), transferenciaDtoResult.getCuentaDestino());
    assertEquals(transferenciaResponseDto.getMonto(), transferenciaDtoResult.getMonto());
    assertEquals(transferenciaResponseDto.getMoneda(), transferenciaDtoResult.getMoneda());
    assertEquals(
        transferenciaResponseDto.getMontoDebitado(), transferenciaDtoResult.getMontoDebitado());
    assertEquals(
        transferenciaResponseDto.getDescripcion(), transferenciaDtoResult.getDescripcion());
    assertEquals(
        transferenciaResponseDto.getTipoTransaccion(), transferenciaDtoResult.getTipoTransaccion());
  }

  @Test
  public void testRealizarTransferenciaBancoExternoSuccess()
      throws CuentaNoExistsException,
          MontoInsuficienteException,
          MonedasDistintasException,
          BanelcoErrorException,
          CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException {

    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    Cuenta cuentaOrigen = createCuenta(1);
    TransferenciaResponseDto transferenciaResponseDto = createTransferenciaResponseDto();
    transferenciaResponseDto.setMoneda(cuentaOrigen.getMoneda().toString());

    when(cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen()))
        .thenReturn(cuentaOrigen);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino());

    TransferenciaResponseDto transferenciaDtoResult =
        movimientoService.realizarTransferencia(transferenciaDto);

    assertEquals(
        transferenciaResponseDto.getMovimientoId(), transferenciaDtoResult.getMovimientoId());
    assertEquals(
        transferenciaResponseDto.getCuentaOrigen(), transferenciaDtoResult.getCuentaOrigen());
    assertEquals(
        transferenciaResponseDto.getCuentaDestino(), transferenciaDtoResult.getCuentaDestino());
    assertEquals(transferenciaResponseDto.getMonto(), transferenciaDtoResult.getMonto());
    assertEquals(transferenciaResponseDto.getMoneda(), transferenciaDtoResult.getMoneda());
    assertEquals(
        transferenciaResponseDto.getMontoDebitado(), transferenciaDtoResult.getMontoDebitado());
    assertEquals(
        transferenciaResponseDto.getDescripcion(), transferenciaDtoResult.getDescripcion());
    assertEquals(
        transferenciaResponseDto.getTipoTransaccion(), transferenciaDtoResult.getTipoTransaccion());
  }

  @Test
  public void testGetMontoDebitadoPesosMenorMillon() {
    double monto = 1000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, 0, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto);
  }

  @Test
  public void testGetMontoDebitadoPesosMayorMillon() {
    double monto = 2_000_000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, 0, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto * 1.02);
  }

  @Test
  public void testGetMontoDebitadoDolaresMenorCincoMil() {
    double monto = 2_000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, 0, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto);
  }

  @Test
  public void testGetMontoDebitadoDolaresMayorCincoMil() {
    double monto = 7_000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, 0, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto * 1.005);
  }

  @Test
  public void testRealizarDepositoMonedasDistintasException()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    DepositoRequestDto depositoDto = createDepositoDto();
    Cuenta cuentaOrigen = createCuenta(1);

    when(cuentaService.buscarCuentaCompletaPorId(depositoDto.getCuenta())).thenReturn(cuentaOrigen);

    doThrow(MonedasDistintasException.class)
        .when(movimientoServiceValidator)
        .validateMonedaIngresadaCorrecta(cuentaOrigen, depositoDto);

    assertThrows(
        MonedasDistintasException.class, () -> movimientoService.realizarDeposito(depositoDto));
  }

  @Test
  public void testRealizarDepositoCuentaNoExistsException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    DepositoRequestDto depositoDto = createDepositoDto();

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(depositoDto.getCuenta());

    assertThrows(
        CuentaNoExistsException.class, () -> movimientoService.realizarDeposito(depositoDto));
  }

  @Test
  public void testRealizarDepositoCorruptedDataInDbException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    DepositoRequestDto depositoDto = createDepositoDto();

    doThrow(CorruptedDataInDbException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(depositoDto.getCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> movimientoService.realizarDeposito(depositoDto));
  }

  @Test
  public void testRealizarDepositoBuscarCuentaImpossibleException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    DepositoRequestDto depositoDto = createDepositoDto();

    doThrow(ImpossibleException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(depositoDto.getCuenta());

    assertThrows(ImpossibleException.class, () -> movimientoService.realizarDeposito(depositoDto));
  }

  @Test
  public void testRealizarDepositoAgregarMovimientoACuentasImpossibleException()
      throws ImpossibleException {
    DepositoRequestDto depositoDto = createDepositoDto();

    doThrow(ImpossibleException.class)
        .when(cuentaService)
        .agregarMovimientoACuentas(any(MovimientoUnidireccional.class));

    assertThrows(ImpossibleException.class, () -> movimientoService.realizarDeposito(depositoDto));
  }

  @Test
  public void testRealizarDepositoSuccess()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    DepositoRequestDto depositoDto = createDepositoDto();
    Cuenta cuentaOrigen = createCuenta(1);
    DepositoResponseDto depositoResponseDto = createDepositoResponseDto();
    depositoResponseDto.setMoneda(cuentaOrigen.getMoneda().toString());

    when(cuentaService.buscarCuentaCompletaPorId(depositoDto.getCuenta())).thenReturn(cuentaOrigen);

    DepositoResponseDto depositoDtoResult = movimientoService.realizarDeposito(depositoDto);

    verify(movimientoDao, times(1)).save(any(Movimiento.class));
    assertEquals(depositoResponseDto.getMovimientoId(), depositoDtoResult.getMovimientoId());
    assertEquals(depositoResponseDto.getTipoTransaccion(), depositoDtoResult.getTipoTransaccion());
    assertEquals(depositoResponseDto.getDescripcion(), depositoDtoResult.getDescripcion());
    assertEquals(depositoResponseDto.getMonto(), depositoDtoResult.getMonto());
    assertEquals(depositoResponseDto.getMontoDebitado(), depositoDtoResult.getMontoDebitado());
    assertEquals(depositoResponseDto.getMoneda(), depositoDtoResult.getMoneda());
    assertEquals(depositoResponseDto.getCuenta(), depositoDtoResult.getCuenta());
  }

  @Test
  public void testRealizarRetiroMonedasDistintasException()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException {
    RetiroRequestDto retiroDto = createRetiroDto();
    Cuenta cuentaOrigen = createCuenta(1);

    when(cuentaService.buscarCuentaCompletaPorId(retiroDto.getCuenta())).thenReturn(cuentaOrigen);

    doThrow(MonedasDistintasException.class)
        .when(movimientoServiceValidator)
        .validateMonedaIngresadaCorrecta(cuentaOrigen, retiroDto);

    assertThrows(
        MonedasDistintasException.class, () -> movimientoService.realizarRetiro(retiroDto));
  }

  @Test
  public void testRealizarRetiroCuentaNoExistsException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    RetiroRequestDto retiroDto = createRetiroDto();

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(retiroDto.getCuenta());

    assertThrows(CuentaNoExistsException.class, () -> movimientoService.realizarRetiro(retiroDto));
  }

  @Test
  public void testRealizarRetiroCorruptedDataInDbException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    RetiroRequestDto retiroDto = createRetiroDto();

    doThrow(CorruptedDataInDbException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(retiroDto.getCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> movimientoService.realizarRetiro(retiroDto));
  }

  @Test
  public void testRealizarRetiroBuscarCuentaImpossibleException()
      throws CuentaNoExistsException, CorruptedDataInDbException, ImpossibleException {
    RetiroRequestDto retiroDto = createRetiroDto();

    doThrow(ImpossibleException.class)
        .when(cuentaService)
        .buscarCuentaCompletaPorId(retiroDto.getCuenta());

    assertThrows(ImpossibleException.class, () -> movimientoService.realizarRetiro(retiroDto));
  }

  @Test
  public void testRealizarRetiroAgregarMovimientoACuentasImpossibleException()
      throws ImpossibleException {
    RetiroRequestDto retiroDto = createRetiroDto();

    doThrow(ImpossibleException.class)
        .when(cuentaService)
        .agregarMovimientoACuentas(any(MovimientoUnidireccional.class));

    assertThrows(ImpossibleException.class, () -> movimientoService.realizarRetiro(retiroDto));
  }

  @Test
  public void testRealizarRetiroMontoInsuficienteException()
      throws ImpossibleException, MontoInsuficienteException {
    RetiroRequestDto retiroDto = createRetiroDto();

    doThrow(MontoInsuficienteException.class)
        .when(movimientoServiceValidator)
        .validateMonto(any(Retiro.class));

    assertThrows(
        MontoInsuficienteException.class, () -> movimientoService.realizarRetiro(retiroDto));
  }

  @Test
  public void testRealizarRetiroSuccess()
      throws MonedasDistintasException,
          CuentaNoExistsException,
          CorruptedDataInDbException,
          ImpossibleException,
          MontoInsuficienteException {
    RetiroRequestDto retiroDto = createRetiroDto();
    Cuenta cuentaOrigen = createCuenta(1);
    RetiroResponseDto retiroResponseDto = createRetiroResponseDto();
    retiroResponseDto.setMoneda(cuentaOrigen.getMoneda().toString());

    when(cuentaService.buscarCuentaCompletaPorId(retiroDto.getCuenta())).thenReturn(cuentaOrigen);

    RetiroResponseDto retiroDtoResult = movimientoService.realizarRetiro(retiroDto);

    verify(movimientoDao, times(1)).save(any(Movimiento.class));
    assertEquals(retiroResponseDto.getMovimientoId(), retiroDtoResult.getMovimientoId());
    assertEquals(retiroResponseDto.getTipoTransaccion(), retiroDtoResult.getTipoTransaccion());
    assertEquals(retiroResponseDto.getDescripcion(), retiroDtoResult.getDescripcion());
    assertEquals(retiroResponseDto.getMonto(), retiroDtoResult.getMonto());
    assertEquals(retiroResponseDto.getMontoDebitado(), retiroDtoResult.getMontoDebitado());
    assertEquals(retiroResponseDto.getMoneda(), retiroDtoResult.getMoneda());
    assertEquals(retiroResponseDto.getCuenta(), retiroDtoResult.getCuenta());
  }

  private Cuenta createCuenta(int id) {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(id);
    cuenta.setBalance(500000);
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuenta.setActivo(true);
    return cuenta;
  }

  private TransferenciaRequestDto createTransferenciaDto() {
    TransferenciaRequestDto transferenciaDto = new TransferenciaRequestDto();
    transferenciaDto.setCuentaOrigen(1);
    transferenciaDto.setCuentaDestino(2);
    transferenciaDto.setMonto(1000);
    return transferenciaDto;
  }

  private TransferenciaResponseDto createTransferenciaResponseDto() {
    TransferenciaResponseDto transferenciaResponseDto = new TransferenciaResponseDto();
    transferenciaResponseDto.setCuentaOrigen(1);
    transferenciaResponseDto.setCuentaDestino(2);
    transferenciaResponseDto.setMonto(1000);
    transferenciaResponseDto.setMontoDebitado(1000);
    transferenciaResponseDto.setMoneda("P");
    transferenciaResponseDto.setDescripcion("Transferencia");
    return transferenciaResponseDto;
  }

  private BanelcoResponseDto createBanelcoResponse() {
    return new BanelcoResponseDto();
  }

  private DepositoRequestDto createDepositoDto() {
    DepositoRequestDto depositoDto = new DepositoRequestDto();
    depositoDto.setCuenta(1);
    depositoDto.setMonto(1000);
    depositoDto.setMoneda("D");
    return depositoDto;
  }

  private DepositoResponseDto createDepositoResponseDto() {
    DepositoResponseDto depositoResponseDto = new DepositoResponseDto();
    depositoResponseDto.setCuenta(1);
    depositoResponseDto.setMonto(1000);
    depositoResponseDto.setMoneda("D");
    depositoResponseDto.setDescripcion("Deposito");
    return depositoResponseDto;
  }

  private RetiroRequestDto createRetiroDto() {
    RetiroRequestDto retiroDto = new RetiroRequestDto();
    retiroDto.setCuenta(1);
    retiroDto.setMonto(1000);
    retiroDto.setMoneda("D");
    return retiroDto;
  }

  private RetiroResponseDto createRetiroResponseDto() {
    RetiroResponseDto retiroResponseDto = new RetiroResponseDto();
    retiroResponseDto.setCuenta(1);
    retiroResponseDto.setMonto(1000);
    retiroResponseDto.setMoneda("D");
    retiroResponseDto.setDescripcion("Retiro");
    return retiroResponseDto;
  }
}
