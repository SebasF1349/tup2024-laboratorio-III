package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.externalService.Banelco;
import ar.edu.utn.frbb.tup.externalService.BanelcoResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.validator.MovimientoServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientoService {
  MovimientoDao movimientoDao = new MovimientoDao();
  Banelco banelco = new Banelco();

  @Autowired MovimientoServiceValidator movimientoServiceValidator;
  @Autowired CuentaService cuentaService;

  public TransferenciaResponseDto realizarTransferencia(TransferenciaDto transferenciaDto)
      throws CuentaNoExistsException,
          MontoInsuficienteException,
          MonedasDistintasException,
          CorruptedDataInDbException,
          BanelcoErrorException {

    Cuenta cuentaOrigen =
        cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaOrigen());

    movimientoServiceValidator.validateMonedaIngresadaCorrecta(cuentaOrigen, transferenciaDto);

    Transferencia transferencia = new Transferencia(transferenciaDto, cuentaOrigen);

    try {
      Cuenta cuentaDestino =
          cuentaService.buscarCuentaCompletaPorId(transferenciaDto.getCuentaDestino());

      transferencia.setCuentaDestino(cuentaDestino);

      movimientoServiceValidator.validateSameMonedaBetweenCuentas(transferencia);
    } catch (CuentaNoExistsException ex) {
      BanelcoResponseDto banelcoResponse =
          banelco.transferir(
              cuentaOrigen, transferenciaDto.getCuentaDestino(), transferenciaDto.getMonto());

      movimientoServiceValidator.validateBanelcoResponse(banelcoResponse);

      Cuenta cuentaExterna = new Cuenta();
      cuentaExterna.setNumeroCuenta(transferenciaDto.getCuentaDestino());
      transferencia.setCuentaDestino(cuentaExterna);
    }

    transferencia.setMontoDebitado(getMontoDebitado(transferencia));

    movimientoServiceValidator.validateMonto(transferencia);

    cuentaService.agregarTransferenciaACuentas(transferencia);
    movimientoDao.save(transferencia);
    return transferencia.toTransferenciaResponseDto();
  }

  protected double getMontoDebitado(Transferencia transferencia) {
    double nuevoMonto;
    if (transferencia.getCuenta().getMoneda().equals(TipoMoneda.PESOS_ARGENTINOS)
        && transferencia.getMonto() > 1_000_000) {
      nuevoMonto = transferencia.getMonto() * 1.02;
    } else if (transferencia.getCuenta().getMoneda().equals(TipoMoneda.DOLARES_AMERICANOS)
        && transferencia.getMonto() > 5_000) {
      nuevoMonto = transferencia.getMonto() * 1.005;
    } else {
      nuevoMonto = transferencia.getMonto();
    }
    return nuevoMonto;
  }
}
