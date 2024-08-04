package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.ClienteRequestDto;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ClienteControllerValidator {

  public void validate(ClienteRequestDto clienteDto) throws WrongInputDataException {
    validateTipoPersona(clienteDto);
    validateStringWithOnlyLetters(clienteDto.getNombre(), "nombre");
    validateStringWithOnlyLetters(clienteDto.getApellido(), "apellido");
    validateFechaNacimiento(clienteDto);
  }

  public void validateTipoPersona(ClienteRequestDto clienteDto) throws WrongInputDataException {
    try {
      TipoPersona.fromString(clienteDto.getTipoPersona());
    } catch (IllegalArgumentException ex) {
      throw new WrongInputDataException("El tipo de persona no es correcto");
    }
  }

  public void validateStringWithOnlyLetters(String str, String msg) throws WrongInputDataException {
    if (!str.matches("[a-zA-Z \\-']+")) {
      throw new WrongInputDataException("Error en el formato del " + msg);
    }
  }

  public void validateStringWithOnlyNumbers(String str, String msg) throws WrongInputDataException {
    if (!str.matches("\\d+")) {
      throw new WrongInputDataException("Error en el formato del " + msg);
    }
  }

  public void validateFechaNacimiento(ClienteRequestDto clienteDto) throws WrongInputDataException {
    try {
      LocalDate.parse(clienteDto.getFechaNacimiento());
    } catch (Exception e) {
      throw new WrongInputDataException("Error en el formato de la fecha de nacimiento");
    }
  }
}
