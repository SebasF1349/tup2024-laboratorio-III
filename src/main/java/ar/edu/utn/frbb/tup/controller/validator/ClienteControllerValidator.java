package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ClienteControllerValidator {

  public void validate(ClienteDto clienteDto) throws WrongInputDataException {
    // NOTE: validate dni???
    validateTipoPersona(clienteDto);
    validateStringWithOnlyLetters(clienteDto.getNombre(), "nombre");
    validateStringWithOnlyLetters(clienteDto.getApellido(), "apellido");
    validateFechaNacimiento(clienteDto);
  }

  public void validateTipoPersona(ClienteDto clienteDto) throws WrongInputDataException {
    if (!"F".equals(clienteDto.getTipoPersona()) && !"J".equals(clienteDto.getTipoPersona())) {
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

  public void validateFechaNacimiento(ClienteDto clienteDto) throws WrongInputDataException {
    try {
      LocalDate.parse(clienteDto.getFechaNacimiento());
    } catch (Exception e) {
      throw new WrongInputDataException("Error en el formato de la fecha de nacimiento");
    }
  }
}
