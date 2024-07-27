package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import java.time.LocalDateTime;

public class MovimientoEntity extends BaseEntity {
  private String movimiento;
  private long numeroCuenta;
  private LocalDateTime diaHora;
  private double monto;
  private long numeroCuentaDestino;
  private String descripcion;

  public MovimientoEntity(Movimiento movimiento) {
    super(movimiento.getMovimientoId());
    this.numeroCuenta = movimiento.getCuenta().getNumeroCuenta();
    this.diaHora = movimiento.getDiaHora();
    this.monto = movimiento.getMonto();
    this.movimiento = movimiento.getTipoMovimiento();
    this.descripcion = movimiento.getDescripcion();
    if (movimiento instanceof Transferencia) {
      this.numeroCuentaDestino = ((Transferencia) movimiento).getCuentaDestino().getNumeroCuenta();
    }
  }

  public Movimiento toMovimiento() {
    CuentaDao cuentaDao = new CuentaDao();
    Cuenta cuenta = cuentaDao.find(this.numeroCuenta, true);
    Movimiento movimiento;
    switch (this.movimiento) {
      case "TRANSFERENCIA":
        Cuenta cuentaDestino = cuentaDao.find(this.numeroCuentaDestino, true);
        movimiento =
            new Transferencia(
                this.monto, cuentaDestino, this.diaHora, this.getId(), cuenta, this.descripcion);
        break;
      case "RETIRO":
        movimiento = new Retiro(this.monto, this.diaHora, this.getId(), cuenta, this.descripcion);
        break;
      case "DEPOSITO":
        movimiento = new Deposito(this.monto, this.diaHora, this.getId(), cuenta, this.descripcion);
        break;
      default:
        throw new IllegalArgumentException("Unhandled TipoMovimiento");
    }
    return movimiento;
  }
}
