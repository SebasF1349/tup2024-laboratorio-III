package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cuenta {
  String id;
  LocalDateTime fechaApertura;
  int saldo;
  TipoCuenta tipoCuenta;
  MonedaCuenta moneda;
  List<Movimiento> movimientos;

  public LocalDateTime getFechaApertura() {
    return fechaApertura;
  }

  public Cuenta setFechaApertura(LocalDateTime fechaCreacion) {
    this.fechaApertura = fechaCreacion;
    return this;
  }

  public int getSaldo() {
    return saldo;
  }

  public Cuenta setSaldo(int balance) {
    this.saldo = balance;
    return this;
  }

  public TipoCuenta getTipoCuenta() {
    return tipoCuenta;
  }

  public void setTipoCuenta(TipoCuenta tipoCuenta) {
    this.tipoCuenta = tipoCuenta;
  }

  public MonedaCuenta getMoneda() {
    return moneda;
  }

  public void setMoneda(MonedaCuenta moneda) {
    this.moneda = moneda;
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public boolean addMovimiento(Movimiento movimiento) {
    return this.movimientos.add(movimiento);
  }
}
