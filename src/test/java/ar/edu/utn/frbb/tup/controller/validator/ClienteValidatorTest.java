package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
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
public class ClienteValidatorTest {

  @InjectMocks private ClienteValidator clienteValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidateTipoPersonaFail() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setTipoPersona("A");

    assertThrows(
        WrongInputDataException.class, () -> clienteValidator.validateTipoPersona(clienteDto));
  }

  @Test
  public void testValidateTipoPersonaFSuccess() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setTipoPersona("F");

    assertDoesNotThrow(() -> clienteValidator.validateTipoPersona(clienteDto));
  }

  @Test
  public void testValidateTipoPersonaJSuccess() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setTipoPersona("J");

    assertDoesNotThrow(() -> clienteValidator.validateTipoPersona(clienteDto));
  }

  @Test
  public void testValidateStringWithOnlyLettersFail() {
    assertThrows(
        WrongInputDataException.class,
        () -> clienteValidator.validateStringWithOnlyLetters("abc123", ""));
  }

  @Test
  public void testValidateStringWithOnlyLettersSuccess() {
    assertDoesNotThrow(() -> clienteValidator.validateStringWithOnlyLetters("abcdef", ""));
  }

  @Test
  public void testValidateDniWithOnlyNumbersFail() {
    String dni = "abc123";

    assertThrows(
        WrongInputDataException.class,
        () -> clienteValidator.validateStringWithOnlyNumbers(dni, ""));
  }

  @Test
  public void testValidateDniWithOnlyNumbersSuccess() {
    String dni = "123456";

    assertDoesNotThrow(() -> clienteValidator.validateStringWithOnlyNumbers(dni, ""));
  }

  @Test
  public void testValidateFechaNacimientoYearFail() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setFechaNacimiento("90-01-01");

    assertThrows(
        WrongInputDataException.class, () -> clienteValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoMonthOrderFail() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setFechaNacimiento("1990-23-01");

    assertThrows(
        WrongInputDataException.class, () -> clienteValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoOneDigitFail() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setFechaNacimiento("1990-1-01");

    assertThrows(
        WrongInputDataException.class, () -> clienteValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoYearOrderFail() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setFechaNacimiento("01-01-1990");

    assertThrows(
        WrongInputDataException.class, () -> clienteValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoSuccess() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setFechaNacimiento("1990-12-30");

    assertDoesNotThrow(() -> clienteValidator.validateFechaNacimiento(clienteDto));
  }
}
