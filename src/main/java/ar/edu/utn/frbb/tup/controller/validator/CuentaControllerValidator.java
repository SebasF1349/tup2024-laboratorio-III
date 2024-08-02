package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.CuentaRequestDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.springframework.stereotype.Component;

@Component
public class CuentaControllerValidator {
  public void validate(CuentaRequestDto cuentaRequestDto) throws WrongInputDataException {
    validateTipoCuenta(cuentaRequestDto);
    validateMoneda(cuentaRequestDto);
  }

  public void validateTipoCuenta(CuentaRequestDto cuentaRequestDto) throws WrongInputDataException {
    if (!"C".equals(cuentaRequestDto.getTipoCuenta())
        && !"A".equals(cuentaRequestDto.getTipoCuenta())) {
      throw new WrongInputDataException("El tipo de cuenta no es correcto");
    }
  }

  public void validateMoneda(CuentaRequestDto cuentaRequestDto) throws WrongInputDataException {
    if (!"P".equals(cuentaRequestDto.getMoneda()) && !"D".equals(cuentaRequestDto.getMoneda())) {
      throw new WrongInputDataException("La moneda no es correcta");
    }
  }
}
