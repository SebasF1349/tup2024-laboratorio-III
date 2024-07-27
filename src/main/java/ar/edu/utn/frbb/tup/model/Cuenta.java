package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Cuenta {
  private long numeroCuenta;
  private LocalDateTime fechaApertura;
  private double balance;
  private TipoCuenta tipoCuenta;
  private TipoMoneda moneda;
  private Cliente titular;
  private Set<Movimiento> movimientos;
  private boolean activo;

  public Cuenta() {
    this.fechaApertura = LocalDateTime.now();
    this.balance = 0;
    this.movimientos = new HashSet<>();
  }

  public Cuenta(TipoCuenta tipoCuenta, TipoMoneda tipoMoneda, Cliente titular) {
    this.fechaApertura = LocalDateTime.now();
    this.balance = 0;
    this.tipoCuenta = tipoCuenta;
    this.moneda = tipoMoneda;
    this.movimientos = new HashSet<>();
    this.titular = titular;
  }

  public Cuenta(CuentaDto cuentaDto) {
    this.fechaApertura = LocalDateTime.now();
    this.balance = cuentaDto.getBalance();
    this.movimientos = new HashSet<>();
    this.titular = null;
    this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
    this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
  }

  public CuentaDto toCuentaDto() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setBalance(this.balance);
    cuentaDto.setTipoCuenta(this.tipoCuenta.toString());
    cuentaDto.setMoneda(this.moneda.toString());
    cuentaDto.setTitular(this.titular.getDni());
    return cuentaDto;
  }

  public long getNumeroCuenta() {
    return numeroCuenta;
  }

  public Cuenta setNumeroCuenta(long numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
    return this;
  }

  public Cliente getTitular() {
    return titular;
  }

  public Cuenta setTitular(Cliente titular) {
    this.titular = titular;
    return this;
  }

  public LocalDateTime getFechaApertura() {
    return fechaApertura;
  }

  public Cuenta setFechaApertura(LocalDateTime fechaCreacion) {
    this.fechaApertura = fechaCreacion;
    return this;
  }

  public double getBalance() {
    return balance;
  }

  public Cuenta setBalance(double balance) {
    this.balance = balance;
    return this;
  }

  public TipoCuenta getTipoCuenta() {
    return tipoCuenta;
  }

  public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
    this.tipoCuenta = tipoCuenta;
    return this;
  }

  public TipoMoneda getMoneda() {
    return moneda;
  }

  public Cuenta setMoneda(TipoMoneda moneda) {
    this.moneda = moneda;
    return this;
  }

  public Set<Movimiento> getMovimientos() {
    return movimientos;
  }

  public Movimiento getMovimiento(long movimientoId) {
    for (Movimiento movimiento : movimientos) {
      if (movimiento.getMovimientoId() == movimientoId) {
        return movimiento;
      }
    }
    return null;
  }

  public Cuenta setMovimientos(Set<Movimiento> movimientos) {
    this.movimientos = movimientos;
    return this;
  }

  public void addMovimiento(Movimiento movimiento) throws IllegalArgumentException {
    this.movimientos.add(movimiento);
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  @Override
  public String toString() {
    return "Cuenta{numeroCuenta="
        + numeroCuenta
        + ", fechaApertura="
        + fechaApertura
        + ", balance="
        + balance
        + ", tipoCuenta="
        + tipoCuenta
        + ", moneda="
        + moneda
        + ", titular="
        + titular
        + ", movimientos="
        + movimientos
        + ", activo="
        + activo
        + "}";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (numeroCuenta ^ (numeroCuenta >>> 32));
    result = prime * result + ((fechaApertura == null) ? 0 : fechaApertura.hashCode());
    long temp;
    temp = Double.doubleToLongBits(balance);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((tipoCuenta == null) ? 0 : tipoCuenta.hashCode());
    result = prime * result + ((moneda == null) ? 0 : moneda.hashCode());
    result = prime * result + ((titular == null) ? 0 : titular.hashCode());
    result = prime * result + ((movimientos == null) ? 0 : movimientos.hashCode());
    result = prime * result + (activo ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Cuenta other = (Cuenta) obj;
    if (numeroCuenta != other.numeroCuenta) {
      return false;
    }
    if (fechaApertura == null) {
      if (other.fechaApertura != null) {
        return false;
      }
    } else if (!fechaApertura.equals(other.fechaApertura)) {
      return false;
    }
    if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance)) {
      return false;
    }
    if (tipoCuenta != other.tipoCuenta) {
      return false;
    }
    if (moneda != other.moneda) {
      return false;
    }
    if (titular == null) {
      if (other.titular != null) {
        return false;
      }
    } else if (!titular.equals(other.titular)) {
      return false;
    }
    if (movimientos == null) {
      if (other.movimientos != null) {
        return false;
      }
    } else if (!movimientos.equals(other.movimientos)) {
      return false;
    }
    if (activo != other.activo) {
      return false;
    }
    return true;
  }
}
