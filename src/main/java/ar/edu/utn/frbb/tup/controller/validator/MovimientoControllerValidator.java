package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.springframework.stereotype.Component;

@Component
public class MovimientoControllerValidator {
  public void validate(TransferenciaDto transferenciaDto) throws WrongInputDataException {
    validateMoneda(transferenciaDto);
  }

  // TODO: pdf shows it should accept "dolares" too
  public void validateMoneda(TransferenciaDto transferenciaDto) throws WrongInputDataException {
    if (!"P".equals(transferenciaDto.getMoneda()) && !"D".equals(transferenciaDto.getMoneda())) {
      throw new WrongInputDataException("La moneda no es correcta");
    }
  }
}
