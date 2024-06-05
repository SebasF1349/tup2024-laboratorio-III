package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Cuenta {
  private String numeroCuenta;
  private LocalDateTime fechaApertura;
  private double balance;
  private TipoCuenta tipoCuenta;
  private MonedaCuenta moneda;
  private Cliente titular;
  private Set<Movimiento> movimientos;

  public Cuenta() {
    this.numeroCuenta = this.getRandomId();
    this.fechaApertura = LocalDateTime.now();
    this.balance = 0;
    this.movimientos = new HashSet<>();
    this.titular = null;
  }

  public Cuenta(TipoCuenta tipoCuenta, MonedaCuenta moneda, Cliente titular) {
    this.numeroCuenta = this.getRandomId();
    this.fechaApertura = LocalDateTime.now();
    this.balance = 0;
    this.tipoCuenta = tipoCuenta;
    this.moneda = moneda;
    this.movimientos = new HashSet<>();
    this.titular = titular;
  }

  public String getNumeroCuenta() {
    return numeroCuenta;
  }

  public void setNumeroCuenta(String numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

  public Cliente getTitular() {
    return titular;
  }

  public void setTitular(Cliente titular) {
    this.titular = titular;
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

  public void setTipoCuenta(TipoCuenta tipoCuenta) {
    this.tipoCuenta = tipoCuenta;
  }

  public MonedaCuenta getMoneda() {
    return moneda;
  }

  public void setMoneda(MonedaCuenta moneda) {
    this.moneda = moneda;
  }

  public Set<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(Set<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void addMovimiento(Movimiento movimiento) throws IllegalArgumentException {
    this.movimientos.add(movimiento);
  }

  private String getRandomId() {
    return UUID.randomUUID().toString();
  }

  @Override
  public String toString() {
    return "ID: " + numeroCuenta + ", Tipo de cuenta: " + tipoCuenta + ", Moneda: " + moneda;
  }
}
