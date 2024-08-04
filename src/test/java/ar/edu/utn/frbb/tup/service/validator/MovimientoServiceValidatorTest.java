package ar.edu.utn.frbb.tup.service.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frbb.tup.externalService.BanelcoResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
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
public class MovimientoServiceValidatorTest {
  @Mock private MovimientoDao movimientoDao;

  @InjectMocks private MovimientoServiceValidator movimientoServiceValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void validateMontoInsuficienteException() {
    Transferencia transferencia = createTransferencia();
    transferencia.getCuenta().setBalance(10);
    transferencia.setMontoDebitado(50);

    assertThrows(
        MontoInsuficienteException.class,
        () -> movimientoServiceValidator.validateMonto(transferencia));
  }

  @Test
  public void validateMontoTransferenciaSuccess() {
    Transferencia transferencia = createTransferencia();
    transferencia.getCuenta().setBalance(100);
    transferencia.setMontoDebitado(50);

    assertDoesNotThrow(() -> movimientoServiceValidator.validateMonto(transferencia));
  }

  @Test
  public void validateMontoRetiroSuccess() {
    Retiro retiro = createRetiro();
    retiro.getCuenta().setBalance(100);
    retiro.setMonto(50);

    assertDoesNotThrow(() -> movimientoServiceValidator.validateMonto(retiro));
  }

  @Test
  public void validateSameMonedaDistintasException() {
    Transferencia transferencia = createTransferencia();
    transferencia.getCuenta().setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    transferencia.getCuentaDestino().setMoneda(TipoMoneda.DOLARES_AMERICANOS);

    assertThrows(
        MonedasDistintasException.class,
        () -> movimientoServiceValidator.validateSameMonedaBetweenCuentas(transferencia));
  }

  @Test
  public void validateSameMonedaSuccess() {
    Transferencia transferencia = createTransferencia();
    transferencia.getCuenta().setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    transferencia.getCuentaDestino().setMoneda(TipoMoneda.PESOS_ARGENTINOS);

    assertDoesNotThrow(
        () -> movimientoServiceValidator.validateSameMonedaBetweenCuentas(transferencia));
  }

  @Test
  public void validateBanelcoResponseCuentaNoExistsException() {
    BanelcoResponseDto banelcoResponse = new BanelcoResponseDto(400, 1, "");

    assertThrows(
        CuentaNoExistsException.class,
        () -> movimientoServiceValidator.validateBanelcoResponse(banelcoResponse));
  }

  @Test
  public void validateBanelcoResponseMonedasDistintasException() {
    BanelcoResponseDto banelcoResponse = new BanelcoResponseDto(400, 2, "");

    assertThrows(
        MonedasDistintasException.class,
        () -> movimientoServiceValidator.validateBanelcoResponse(banelcoResponse));
  }

  @Test
  public void validateBanelcoResponseBanelcoError400Exception() {
    BanelcoResponseDto banelcoResponse = new BanelcoResponseDto(400, 3, "");

    assertThrows(
        BanelcoErrorException.class,
        () -> movimientoServiceValidator.validateBanelcoResponse(banelcoResponse));
  }

  @Test
  public void validateBanelcoResponseBanelcoErrorException() {
    BanelcoResponseDto banelcoResponse = new BanelcoResponseDto(500, 1, "");

    assertThrows(
        BanelcoErrorException.class,
        () -> movimientoServiceValidator.validateBanelcoResponse(banelcoResponse));
  }

  @Test
  public void validateBanelcoResponseSuccess() {
    BanelcoResponseDto banelcoResponse = new BanelcoResponseDto(200, 1, "");

    assertDoesNotThrow(() -> movimientoServiceValidator.validateBanelcoResponse(banelcoResponse));
  }

  private Cuenta createCuenta(int id) {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1);
    cuenta.setBalance(500000);
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuenta.setActivo(true);
    return cuenta;
  }

  private Transferencia createTransferencia() {
    return new Transferencia(1000, 1000, createCuenta(1), createCuenta(2));
  }

  private Retiro createRetiro() {
    return new Retiro(1000, createCuenta(1));
  }
}
