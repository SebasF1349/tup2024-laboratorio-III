package ar.edu.utn.frbb.tup.model;

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

  public Cuenta() {
    this.fechaApertura = LocalDateTime.now();
    this.balance = 0;
    this.movimientos = new HashSet<>();
    this.titular = null;
  }

  public Cuenta(TipoCuenta tipoCuenta, TipoMoneda tipoMoneda, Cliente titular) {
    this.fechaApertura = LocalDateTime.now();
    this.balance = 0;
    this.tipoCuenta = tipoCuenta;
    this.moneda = tipoMoneda;
    this.movimientos = new HashSet<>();
    this.titular = titular;
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

  public Cuenta setMovimientos(Set<Movimiento> movimientos) {
    this.movimientos = movimientos;
    return this;
  }

  public void addMovimiento(Movimiento movimiento) throws IllegalArgumentException {
    this.movimientos.add(movimiento);
  }

  @Override
  public String toString() {
    return "ID: " + numeroCuenta + ", Tipo de cuenta: " + tipoCuenta + ", Moneda: " + moneda;
  }
}
