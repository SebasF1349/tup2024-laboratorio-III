package ar.edu.utn.frbb.tup.service.validator;

import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.externalService.BanelcoResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import org.springframework.stereotype.Service;

@Service
public class MovimientoServiceValidator {

  public void validateMonto(Transferencia transferencia) throws MontoInsuficienteException {
    if (transferencia.getMontoDebitado() > transferencia.getCuenta().getBalance()) {
      throw new MontoInsuficienteException(
          "La cuenta "
              + transferencia.getCuenta().getNumeroCuenta()
              + " no tiene saldo suficiente para realizar la transferencia.");
    }
  }

  public void validateMonedaIngresadaCorrecta(
      Cuenta cuentaOrigen, TransferenciaDto transferenciaDto) throws MonedasDistintasException {
    if (!cuentaOrigen.getMoneda().equals(TipoMoneda.fromString(transferenciaDto.getMoneda()))) {
      throw new MonedasDistintasException("Las cuenta no poseen la moneda requerida.");
    }
  }

  public void validateSameMonedaBetweenCuentas(Transferencia transferencia)
      throws MonedasDistintasException {
    if (!transferencia
        .getCuenta()
        .getMoneda()
        .equals(transferencia.getCuentaDestino().getMoneda())) {
      throw new MonedasDistintasException("Las cuentas no poseen la misma moneda");
    }
  }

  public void validateBanelcoResponse(BanelcoResponseDto banelcoResponse)
      throws CuentaNoExistsException, MonedasDistintasException, BanelcoErrorException {
    int statusCode = banelcoResponse.getStatusCode();
    int code = banelcoResponse.getInternalCode();
    if (statusCode == 200) {
      return;
    }
    if (statusCode == 400) {
      if (code == 1) {
        throw new CuentaNoExistsException(
            "Cuenta de destino no existe o no esta habilitada para recibir transferencias.");
      } else if (code == 2) {
        throw new MonedasDistintasException("Las cuentas no poseen la misma moneda");
      }
      throw new BanelcoErrorException("Error en la operación", 400);
    }
    throw new BanelcoErrorException("Error en la operación", statusCode);
  }
}
