package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.TransferenciaRequestDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.springframework.stereotype.Component;

@Component
public class MovimientoControllerValidator {
  public void validate(TransferenciaRequestDto transferenciaDto) throws WrongInputDataException {
    validateMoneda(transferenciaDto);
  }

  // TODO: pdf shows it should accept "dolares" too
  public void validateMoneda(TransferenciaRequestDto transferenciaDto)
      throws WrongInputDataException {
    if (!"P".equals(transferenciaDto.getMoneda()) && !"D".equals(transferenciaDto.getMoneda())) {
      throw new WrongInputDataException("La moneda no es correcta");
    }
  }
}
