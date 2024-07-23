package ar.edu.utn.frbb.tup.externalService;

import ar.edu.utn.frbb.tup.model.Cuenta;

public class Banelco {

  public BanelcoResponseDto transferir(Cuenta cuentaOrigen, long cuentaDestino, double monto) {
    int random = (int) ((Math.random() * (10 - 1)) + 1);
    int statusCode;
    int internalCode;
    String message;
    if (random <= 5) {
      statusCode = 400;
      if (random <= 3) {
        internalCode = 1;
        message = "Cuenta de destino no existe o no esta habilitada para recibir transferencias.";
      } else {
        internalCode = 2;
        message = "Las cuentas no poseen la misma moneda";
      }
    } else {
      statusCode = 200;
      internalCode = 0;
      message = "Exito";
    }
    return new BanelcoResponseDto(statusCode, internalCode, message);
  }
}
