package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerValidatorTest {

  @InjectMocks private CuentaControllerValidator cuentaControllerValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidateTipoCuentaFail() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setTipoCuenta("B");

    assertThrows(
        WrongInputDataException.class,
        () -> cuentaControllerValidator.validateTipoCuenta(cuentaDto));
  }

  @Test
  public void testValidateTipoCuentaCSuccess() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setTipoCuenta("C");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateTipoCuenta(cuentaDto));
  }

  @Test
  public void testValidateTipoCuentaASuccess() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setTipoCuenta("A");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateTipoCuenta(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaFail() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setMoneda("A");

    assertThrows(
        WrongInputDataException.class, () -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaPSuccess() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setMoneda("P");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaDJSuccess() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setMoneda("D");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }
}
