package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frbb.tup.controller.CuentaRequestDto;
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
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setTipoCuenta("B");

    assertThrows(
        WrongInputDataException.class,
        () -> cuentaControllerValidator.validateTipoCuenta(cuentaDto));
  }

  @Test
  public void testValidateTipoCuentaCSuccess() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setTipoCuenta("C");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateTipoCuenta(cuentaDto));
  }

  @Test
  public void testValidateTipoCuentaASuccess() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setTipoCuenta("A");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateTipoCuenta(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaFail() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setMoneda("A");

    assertThrows(
        WrongInputDataException.class, () -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaPSuccess() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setMoneda("P");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaPesosSuccess() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setMoneda("Pesos");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaDSuccess() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setMoneda("D");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }

  @Test
  public void testValidateTipoMonedaDolaresSuccess() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setMoneda("dolares");

    assertDoesNotThrow(() -> cuentaControllerValidator.validateMoneda(cuentaDto));
  }
}
