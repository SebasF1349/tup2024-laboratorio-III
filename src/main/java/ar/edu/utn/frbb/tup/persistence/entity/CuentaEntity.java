package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import java.time.LocalDateTime;

public class CuentaEntity extends BaseEntity {
  private LocalDateTime fechaCreacion;
  private double balance;
  private String tipoCuenta;
  private String titular;

  public CuentaEntity(Cuenta cuenta) {
    super(cuenta.getNumeroCuenta());
    this.balance = cuenta.getBalance();
    this.tipoCuenta = cuenta.getTipoCuenta().toString();
    this.titular = cuenta.getTitular().getDni();
    this.fechaCreacion = cuenta.getFechaApertura();
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

    return cuenta;
  }
}
