package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CuentaEntity extends BaseEntity {
  private LocalDateTime fechaCreacion;
  private double balance;
  private String tipoCuenta;
  private String titular;
  private List<String> movimientos;

  public CuentaEntity(Cuenta cuenta) {
    super(cuenta.getNumeroCuenta());
    this.balance = cuenta.getBalance();
    this.tipoCuenta = cuenta.getTipoCuenta().toString();
    this.titular = cuenta.getTitular().getDni();
    this.fechaCreacion = cuenta.getFechaApertura();
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
    cuenta.setBalance(this.balance);
    cuenta.setNumeroCuenta(this.getId());
    cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
    cuenta.setFechaApertura(this.fechaCreacion);

    ClienteDao clienteDao = new ClienteDao();
    Cliente titular = clienteDao.find(this.titular);
    cuenta.setTitular(titular);

    if (!this.movimientos.isEmpty()) {
      Set<Movimiento> m = new HashSet<>();
      MovimientoDao movimientoDao = new MovimientoDao();
      for (String movimientoId : movimientos) {
        Movimiento movimiento = movimientoDao.find(movimientoId);
        m.add(movimiento);
      }
      cuenta.setMovimientos(m);
    }

    return cuenta;
  }
}
