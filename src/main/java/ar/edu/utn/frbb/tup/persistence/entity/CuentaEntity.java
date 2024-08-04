package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CuentaEntity extends BaseEntity {
  private LocalDateTime fechaCreacion;
  private double balance;
  private String tipoCuenta;
  private String moneda;
  private long titular;
  private List<Long> movimientos;
  private boolean activo;
  private boolean externa;

  public CuentaEntity(Cuenta cuenta) {
    super(cuenta.getNumeroCuenta());
    this.externa = cuenta.isExterna();
    if (cuenta.isExterna()) {
      return;
    }
    this.balance = cuenta.getBalance();
    this.tipoCuenta = cuenta.getTipoCuenta().toString();
    this.moneda = cuenta.getMoneda().toString();
    this.titular = cuenta.getTitular().getDni();
    this.fechaCreacion = cuenta.getFechaApertura();
    this.activo = cuenta.isActivo();
    this.movimientos = new ArrayList<>();
    if (cuenta.getMovimientos() != null && !cuenta.getMovimientos().isEmpty()) {
      for (Movimiento m : cuenta.getMovimientos()) {
        movimientos.add(m.getMovimientoId());
      }
    }
  }

  public void addMovimiento(Movimiento movimiento) {
    if (movimiento == null) {
      movimientos = new ArrayList<>();
    }
    movimientos.add(movimiento.getMovimientoId());
  }

  public Cuenta toCuenta() {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(this.getId());
    cuenta.setExterna(this.isExterna());
    if (cuenta.isExterna()) {
      return cuenta;
    }
    cuenta.setBalance(this.balance);
    cuenta.setTipoCuenta(TipoCuenta.fromString(this.tipoCuenta));
    cuenta.setMoneda(TipoMoneda.fromString(this.moneda));
    cuenta.setFechaApertura(this.fechaCreacion);
    cuenta.setActivo(this.activo);

    return cuenta;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public String getTipoCuenta() {
    return tipoCuenta;
  }

  public void setTipoCuenta(String tipoCuenta) {
    this.tipoCuenta = tipoCuenta;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public Long getTitular() {
    return titular;
  }

  public void setTitular(long titular) {
    this.titular = titular;
  }

  public List<Long> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Long> movimientos) {
    this.movimientos = movimientos;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public boolean isExterna() {
    return externa;
  }

  public void setExterna(boolean externa) {
    this.externa = externa;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
    long temp;
    temp = Double.doubleToLongBits(balance);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((tipoCuenta == null) ? 0 : tipoCuenta.hashCode());
    result = prime * result + ((moneda == null) ? 0 : moneda.hashCode());
    result = prime * result + (int) (titular ^ (titular >>> 32));
    result = prime * result + ((movimientos == null) ? 0 : movimientos.hashCode());
    result = prime * result + (activo ? 1231 : 1237);
    result = prime * result + (externa ? 1231 : 1237);
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
    CuentaEntity other = (CuentaEntity) obj;
    if (fechaCreacion == null) {
      if (other.fechaCreacion != null) {
        return false;
      }
    } else if (!fechaCreacion.equals(other.fechaCreacion)) {
      return false;
    }
    if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance)) {
      return false;
    }
    if (tipoCuenta == null) {
      if (other.tipoCuenta != null) {
        return false;
      }
    } else if (!tipoCuenta.equals(other.tipoCuenta)) {
      return false;
    }
    if (moneda == null) {
      if (other.moneda != null) {
        return false;
      }
    } else if (!moneda.equals(other.moneda)) {
      return false;
    }
    if (titular != other.titular) {
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
    if (externa != other.externa) {
      return false;
    }
    return true;
  }
}
