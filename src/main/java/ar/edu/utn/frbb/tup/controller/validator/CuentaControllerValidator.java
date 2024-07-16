package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import org.springframework.stereotype.Component;

@Component
public class CuentaControllerValidator {
  public void validate(CuentaDto cuentaDto) throws WrongInputDataException {
    validateTipoCuenta(cuentaDto);
    validateMoneda(cuentaDto);
  }

  public void validateTipoCuenta(CuentaDto cuentaDto) throws WrongInputDataException {
    if (!"C".equals(cuentaDto.getTipoCuenta()) && !"A".equals(cuentaDto.getTipoCuenta())) {
      throw new WrongInputDataException("El tipo de cuenta no es correcto");
    }
  }

  public void validateMoneda(CuentaDto cuentaDto) throws WrongInputDataException {
    if (!"P".equals(cuentaDto.getMoneda()) && !"D".equals(cuentaDto.getMoneda())) {
      throw new WrongInputDataException("La moneda no es correcta");
    }
  }
}
