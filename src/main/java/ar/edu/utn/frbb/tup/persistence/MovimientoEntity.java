package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.persistence.entity.BaseEntity;
import java.time.LocalDateTime;

public class MovimientoEntity extends BaseEntity {
  private String movimiento;
  private String numeroCuenta;
  private LocalDateTime diaHora;
  private double monto;
  private boolean esCuentaPropia;
  private String numeroCuentaDestino;
  private boolean esDestinatario;

  public MovimientoEntity(Movimiento movimiento) {
    super(movimiento.getMovimientoId());
    this.numeroCuenta = movimiento.getCuenta().getNumeroCuenta();
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
    CuentaDao cuentaDao = new CuentaDao();
    Cuenta cuenta = cuentaDao.find(this.numeroCuenta);
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
                this.getId(),
                cuenta);
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
