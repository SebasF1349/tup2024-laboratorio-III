package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.MovimientoControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MovimientoController {
  @Autowired private MovimientoService movimientoService;

  @Autowired private MovimientoControllerValidator movimientoControllerValidator;

  @PostMapping(value = "/transfer")
  public ResponseEntity<TransferenciaDto> realizarTransferencia(
      @Valid @RequestBody TransferenciaDto transferenciaDto)
      throws WrongInputDataException,
          CuentaNoExistsException,
          MontoInsuficienteException,
          MonedasDistintasException,
          CorruptedDataInDbException,
          BanelcoErrorException {
    movimientoControllerValidator.validate(transferenciaDto);
    TransferenciaDto transferenciaResponse =
        movimientoService.realizarTransferencia(transferenciaDto);
    return new ResponseEntity<TransferenciaDto>(
        transferenciaResponse, new HttpHeaders(), HttpStatus.CREATED);
  }
}
