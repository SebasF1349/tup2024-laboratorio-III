package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.MovimientoControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class MovimientoController {
  @Autowired private MovimientoService movimientoService;

  @Autowired private MovimientoControllerValidator movimientoControllerValidator;

  @PostMapping(value = "/transfer")
  public TransferenciaResponseDto realizarTransferencia(
      @Valid @RequestBody TransferenciaDto transferenciaDto)
      throws WrongInputDataException,
          CuentaNoExistsException,
          MontoInsuficienteException,
          MonedasDistintasException,
          CorruptedDataInDbException,
          BanelcoErrorException,
          ImpossibleException {
    movimientoControllerValidator.validate(transferenciaDto);
    return movimientoService.realizarTransferencia(transferenciaDto);
  }
}
