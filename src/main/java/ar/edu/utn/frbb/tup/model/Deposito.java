package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.DepositoRequestDto;
import ar.edu.utn.frbb.tup.controller.DepositoResponseDto;
import java.time.LocalDateTime;

public class Deposito extends MovimientoUnidireccional {
  public Deposito(double monto, Cuenta cuenta) {
    super(monto, cuenta);
  }

  public Deposito(
      double monto, LocalDateTime diaHora, long movimientoId, Cuenta cuenta, String descripcion) {
    super(monto, diaHora, movimientoId, cuenta, descripcion);
  }

  public Deposito(DepositoRequestDto depositoRequestDto, Cuenta cuenta) {
    super(depositoRequestDto.getMonto(), cuenta);
  }

  @Override
  protected TipoTransaccion getTipoTransaccion() {
    return TipoTransaccion.DEBITO;
  }

  public DepositoResponseDto toDepositoResponseDto() {
    DepositoResponseDto depositoResponseDto = new DepositoResponseDto();
    depositoResponseDto.setCuenta(this.getCuenta().getNumeroCuenta());
    depositoResponseDto.setMovimientoId(this.getMovimientoId());
    depositoResponseDto.setFecha(this.getDiaHora().toString());
    depositoResponseDto.setMoneda(this.getCuenta().getMoneda().toString());
    depositoResponseDto.setCuenta(this.getCuenta().getNumeroCuenta());
    depositoResponseDto.setDescripcion(this.getDescripcion());
    depositoResponseDto.setMonto(this.getMonto());
    depositoResponseDto.setTipoTransaccion(this.getTipoMovimiento());
    return depositoResponseDto;
  }
}
