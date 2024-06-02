package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.persistence.entity.BaseEntity;
import java.time.LocalDateTime;

public class MovimientoEntity extends BaseEntity {
  private String movimiento;
  private long movimientoId;
  private LocalDateTime diaHora;
  private double monto;
  private boolean esCuentaPropia;
  private long numeroCuentaDestino;
  private boolean esDestinatario;

  public MovimientoEntity(Movimiento movimiento) {
    super(movimiento.getMovimientoId());
    this.movimientoId = movimiento.getMovimientoId();
    this.diaHora = movimiento.getDiaHora();
    this.monto = movimiento.getMonto();
    if (movimiento instanceof Transferencia) {
      this.movimiento = "TRANSFERENCIA";
      this.esCuentaPropia = ((Transferencia) movimiento).isEsCuentaPropia();
      this.numeroCuentaDestino = ((Transferencia) movimiento).getNumeroCuentaDestino();
      this.esDestinatario = ((Transferencia) movimiento).isEsDestinatario();
    } else if (movimiento instanceof Retiro) {
      this.movimiento = "RETIRO";
    } else if (movimiento instanceof Deposito) {
      this.movimiento = "DEPOSITO";
    } else {
      throw new IllegalArgumentException("Unhandled Movimiento subclass");
    }
  }

  public Movimiento toMovimiento() {
    Movimiento movimiento;
    switch (this.movimiento) {
      case "TRANSFERENCIA":
        movimiento =
            new Transferencia(
                this.monto,
                this.esCuentaPropia,
                this.numeroCuentaDestino,
                this.esDestinatario,
                this.diaHora,
                this.movimientoId);
        break;
      case "RETIRO":
        movimiento = new Retiro(this.monto, this.diaHora, this.movimientoId);
        break;
      case "DEPOSITO":
        movimiento = new Deposito(this.monto, this.diaHora, this.movimientoId);
        break;
      default:
        throw new IllegalArgumentException("Movimiento Entity mal guardado");
    }
    return movimiento;
  }
}
