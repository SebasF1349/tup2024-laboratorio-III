package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cuenta {
  int id;
  LocalDateTime fechaApertura;
  double saldo;
  TipoCuenta tipoCuenta;
  MonedaCuenta moneda;
  List<Movimiento> movimientos;

  public Cuenta(TipoCuenta tipoCuenta, MonedaCuenta moneda) {
    Random random = new Random();
    int id = random.nextInt(999999);
    this.id = id;
    this.fechaApertura = LocalDateTime.now();
    this.saldo = 0;
    this.tipoCuenta = tipoCuenta;
    this.moneda = moneda;
    this.movimientos = new ArrayList<>();
  }

  public LocalDateTime getFechaApertura() {
    return fechaApertura;
  }

  public Cuenta setFechaApertura(LocalDateTime fechaCreacion) {
    this.fechaApertura = fechaCreacion;
    return this;
  }

  public double getSaldo() {
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
