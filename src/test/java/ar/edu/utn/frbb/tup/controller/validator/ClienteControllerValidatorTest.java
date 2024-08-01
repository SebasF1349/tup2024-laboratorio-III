package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frbb.tup.controller.ClienteRequestDto;
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
public class ClienteControllerValidatorTest {

  @InjectMocks private ClienteControllerValidator clienteControllerValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidateTipoPersonaFail() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setTipoPersona("A");

    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateTipoPersona(clienteDto));
  }

  @Test
  public void testValidateTipoPersonaFSuccess() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setTipoPersona("F");

    assertDoesNotThrow(() -> clienteControllerValidator.validateTipoPersona(clienteDto));
  }

  @Test
  public void testValidateTipoPersonaJSuccess() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setTipoPersona("J");

    assertDoesNotThrow(() -> clienteControllerValidator.validateTipoPersona(clienteDto));
  }

  @Test
  public void testValidateStringWithOnlyLettersFail() {
    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateStringWithOnlyLetters("abc123", ""));
  }

  @Test
  public void testValidateStringWithOnlyLettersSuccess() {
    assertDoesNotThrow(
        () -> clienteControllerValidator.validateStringWithOnlyLetters("abcdef", ""));
  }

  @Test
  public void testValidateDniWithOnlyNumbersFail() {
    String dni = "abc123";

    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateStringWithOnlyNumbers(dni, ""));
  }

  @Test
  public void testValidateDniWithOnlyNumbersSuccess() {
    String dni = "123456";

    assertDoesNotThrow(() -> clienteControllerValidator.validateStringWithOnlyNumbers(dni, ""));
  }

  @Test
  public void testValidateFechaNacimientoYearFail() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setFechaNacimiento("90-01-01");

    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoMonthOrderFail() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setFechaNacimiento("1990-23-01");

    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoOneDigitFail() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setFechaNacimiento("1990-1-01");

    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoYearOrderFail() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setFechaNacimiento("01-01-1990");

    assertThrows(
        WrongInputDataException.class,
        () -> clienteControllerValidator.validateFechaNacimiento(clienteDto));
  }

  @Test
  public void testValidateFechaNacimientoSuccess() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setFechaNacimiento("1990-12-30");

    assertDoesNotThrow(() -> clienteControllerValidator.validateFechaNacimiento(clienteDto));
  }
}
