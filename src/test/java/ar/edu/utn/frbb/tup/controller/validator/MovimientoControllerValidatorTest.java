package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
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
public class MovimientoControllerValidatorTest {

  @InjectMocks private MovimientoControllerValidator movimientoControllerValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidateTipoMonedaFail() {
    TransferenciaDto transferenciaDto = new TransferenciaDto();
    transferenciaDto.setMoneda("A");

    assertThrows(
        WrongInputDataException.class,
        () -> movimientoControllerValidator.validateMoneda(transferenciaDto));
  }

  @Test
  public void testValidateTipoMonedaPSuccess() {
    TransferenciaDto transferenciaDto = new TransferenciaDto();
    transferenciaDto.setMoneda("P");

    assertDoesNotThrow(() -> movimientoControllerValidator.validateMoneda(transferenciaDto));
  }

  @Test
  public void testValidateTipoMonedaDJSuccess() {
    TransferenciaDto transferenciaDto = new TransferenciaDto();
    transferenciaDto.setMoneda("D");

    assertDoesNotThrow(() -> movimientoControllerValidator.validateMoneda(transferenciaDto));
  }
}
