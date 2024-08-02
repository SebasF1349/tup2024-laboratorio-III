package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.externalService.Banelco;
import ar.edu.utn.frbb.tup.externalService.BanelcoResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();

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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();

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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();

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

    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    assertEquals(transferenciaResponseDto, transferenciaDtoResult);
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

    TransferenciaDto transferenciaDto = createTransferenciaDto();
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

    assertEquals(transferenciaResponseDto, transferenciaDtoResult);
  }

  @Test
  public void testGetMontoDebitadoPesosMenorMillon() {
    double monto = 1000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto);
  }

  @Test
  public void testGetMontoDebitadoPesosMayorMillon() {
    double monto = 2_000_000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto * 1.02);
  }

  @Test
  public void testGetMontoDebitadoDolaresMenorCincoMil() {
    double monto = 2_000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto);
  }

  @Test
  public void testGetMontoDebitadoDolaresMayorCincoMil() {
    double monto = 7_000;
    Cuenta cuentaOrigen = createCuenta(1);
    cuentaOrigen.setMoneda(TipoMoneda.DOLARES_AMERICANOS);
    Cuenta cuentaDestino = createCuenta(1);
    Transferencia transferencia = new Transferencia(monto, cuentaDestino, cuentaOrigen);

    assertEquals(movimientoService.getMontoDebitado(transferencia), monto * 1.005);
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

  private TransferenciaDto createTransferenciaDto() {
    TransferenciaDto transferenciaDto = new TransferenciaDto();
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
    return transferenciaResponseDto;
  }

  private BanelcoResponseDto createBanelcoResponse() {
    return new BanelcoResponseDto();
  }
}
