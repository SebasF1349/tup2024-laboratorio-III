package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Cuenta {
  private int id;
  private LocalDateTime fechaApertura;
  private double saldo;
  private TipoCuenta tipoCuenta;
  private MonedaCuenta moneda;
  private Set<Movimiento> movimientos;

  private int getRandomId() {
    Clientes clientes = Clientes.getInstance();
    Random random = new Random();
    while (true) {
      int id = random.nextInt(999999);
      if (!clientes.existsCuentaById(id)) {
        return id;
      }
    }
  }

  public Cuenta(TipoCuenta tipoCuenta, MonedaCuenta moneda) {
    this.id = this.getRandomId();
    this.fechaApertura = LocalDateTime.now();
    this.saldo = 0;
    this.tipoCuenta = tipoCuenta;
    this.moneda = moneda;
    this.movimientos = new HashSet<>();
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

  public Set<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(Set<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void addMovimiento(Movimiento movimiento) {
    this.saldo = movimiento.actualizarCuentaMonto(saldo);
    this.movimientos.add(movimiento);
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "ID: " + id + ", Tipo de cuenta: " + tipoCuenta + ", Moneda: " + moneda;
  }
}
