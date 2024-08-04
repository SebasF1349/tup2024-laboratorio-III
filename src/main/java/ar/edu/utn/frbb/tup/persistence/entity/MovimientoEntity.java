package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Deposito;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Retiro;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import java.time.LocalDateTime;

public class MovimientoEntity extends BaseEntity {
  private String movimiento;
  private long numeroCuenta;
  private LocalDateTime diaHora;
  private double monto;
  private double montoDebitado;
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
      this.montoDebitado = ((Transferencia) movimiento).getMontoDebitado();
    }
  }

  public Movimiento toMovimiento() throws ImpossibleException {
    CuentaDao cuentaDao = new CuentaDao();
    Cuenta cuenta = cuentaDao.find(this.numeroCuenta, false);
    Movimiento movimiento;
    switch (this.movimiento) {
      case "Transferencia":
        Cuenta cuentaDestino = cuentaDao.find(this.numeroCuentaDestino, false);
        movimiento =
            new Transferencia(
                this.monto,
                this.montoDebitado,
                cuentaDestino,
                this.diaHora,
                this.getId(),
                cuenta,
                this.descripcion);
        break;
      case "Credito":
        movimiento = new Retiro(this.monto, this.diaHora, this.getId(), cuenta, this.descripcion);
        break;
      case "Debito":
        movimiento = new Deposito(this.monto, this.diaHora, this.getId(), cuenta, this.descripcion);
        break;
      default:
        throw new ImpossibleException();
    }
    return movimiento;
  }

  public String getMovimiento() {
    return movimiento;
  }

  public void setMovimiento(String movimiento) {
    this.movimiento = movimiento;
  }

  public long getNumeroCuenta() {
    return numeroCuenta;
  }

  public void setNumeroCuenta(long numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

  public LocalDateTime getDiaHora() {
    return diaHora;
  }

  public void setDiaHora(LocalDateTime diaHora) {
    this.diaHora = diaHora;
  }

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public long getNumeroCuentaDestino() {
    return numeroCuentaDestino;
  }

  public void setNumeroCuentaDestino(long numeroCuentaDestino) {
    this.numeroCuentaDestino = numeroCuentaDestino;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public double getMontoDebitado() {
    return montoDebitado;
  }

  public void setMontoDebitado(double montoDebitado) {
    this.montoDebitado = montoDebitado;
  }
}
