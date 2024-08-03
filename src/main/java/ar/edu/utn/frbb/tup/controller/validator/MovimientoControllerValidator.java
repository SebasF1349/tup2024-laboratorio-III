package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.MovimientoRequestDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.springframework.stereotype.Component;

@Component
public class MovimientoControllerValidator {
  public void validate(MovimientoRequestDto movimientoRequestDto) throws WrongInputDataException {
    validateMoneda(movimientoRequestDto);
  }

  // TODO: pdf shows it should accept "dolares" too
  public void validateMoneda(MovimientoRequestDto movimientoRequestDto)
      throws WrongInputDataException {
    if (!"P".equals(movimientoRequestDto.getMoneda())
        && !"D".equals(movimientoRequestDto.getMoneda())) {
      throw new WrongInputDataException("La moneda no es correcta");
    }
  }
}
