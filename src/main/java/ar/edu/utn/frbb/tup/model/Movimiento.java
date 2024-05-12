package ar.edu.utn.frbb.tup.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

public abstract class Movimiento extends Operacion {
  private String id;
  private LocalDateTime diaHora;
  private double monto;

  public Movimiento(double monto) {
    Random random = new Random();
    int randomValue = random.nextInt(999);
    long timestamp = Instant.now().getEpochSecond();
    this.id = String.valueOf(timestamp) + randomValue;
    this.diaHora = LocalDateTime.now();
    this.monto = monto;
  }

  public String getId() {
    return id;
  }

  public LocalDateTime getDiaHora() {
    return diaHora;
  }

  public double getMonto() {
    return monto;
  }

  public abstract TipoMovimiento getTipoMovimiento();

  public double actualizarCuentaMonto(double montoCuenta) {
    switch (getTipoMovimiento()) {
      case DEBITO:
        return montoCuenta + this.getMonto();
      case CREDITO:
        return montoCuenta - this.getMonto();
      default:
        throw new IllegalArgumentException("Unhandled TipoMovimiento");
    }
  }
}
