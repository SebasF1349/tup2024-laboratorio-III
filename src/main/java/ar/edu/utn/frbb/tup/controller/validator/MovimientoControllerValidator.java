package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.MovimientoRequestDto;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.springframework.stereotype.Component;

@Component
public class MovimientoControllerValidator {
  public void validate(MovimientoRequestDto movimientoRequestDto) throws WrongInputDataException {
    validateMoneda(movimientoRequestDto);
  }

  public void validateMoneda(MovimientoRequestDto movimientoRequestDto)
      throws WrongInputDataException {
    try {
      TipoMoneda.fromString(movimientoRequestDto.getMoneda());
    } catch (IllegalArgumentException ex) {
      throw new WrongInputDataException("La moneda no es correcta");
    }
  }
}
