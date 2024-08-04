package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.RetiroRequestDto;
import ar.edu.utn.frbb.tup.controller.RetiroResponseDto;
import java.time.LocalDateTime;

public class Retiro extends MovimientoUnidireccional {
  public Retiro(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public Retiro(
      double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta, String descripcion) {
    super(monto, diaHora, movimientoId, cuenta, descripcion);
  }

  public Retiro(RetiroRequestDto depositoRequestDto, Cuenta cuenta) {
    super(depositoRequestDto.getMonto(), cuenta);
  }

  @Override
  protected TipoTransaccion getTipoTransaccion() {
    return TipoTransaccion.CREDITO;
  }

  public RetiroResponseDto toRetiroResponseDto() {
    RetiroResponseDto retiroResponseDto = new RetiroResponseDto();
    retiroResponseDto.setCuenta(this.getCuenta().getNumeroCuenta());
    retiroResponseDto.setMovimientoId(this.getMovimientoId());
    retiroResponseDto.setFecha(this.getDiaHora().toString());
    retiroResponseDto.setMoneda(this.getCuenta().getMoneda().toString());
    retiroResponseDto.setCuenta(this.getCuenta().getNumeroCuenta());
    retiroResponseDto.setDescripcion(this.getDescripcion());
    retiroResponseDto.setMonto(this.getMonto());
    retiroResponseDto.setTipoTransaccion(this.getTipoTransaccion().toString());
    return retiroResponseDto;
  }
}
