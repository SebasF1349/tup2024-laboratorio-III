package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CuentaDao extends AbstractBaseDao {
  @Override
  protected String getEntityName() {
    return "CUENTA";
  }

  // TODO: Add second argument to get movimientos or not
  public Cuenta find(long numeroCuenta) {
    if (getInMemoryDatabase().get(numeroCuenta) == null) return null;
    return ((CuentaEntity) getInMemoryDatabase().get(numeroCuenta)).toCuenta();
  }

  public void save(Cuenta cuenta) {
    // cuenta.setNumeroCuenta(getInMemoryDatabase().size() + 1);
    CuentaEntity entity = new CuentaEntity(cuenta);
    getInMemoryDatabase().put(entity.getId(), entity);
  }

  public List<Cuenta> getCuentasByCliente(long dni) {
    List<Cuenta> cuentasDelCliente = new ArrayList<>();
    for (Object object : getInMemoryDatabase().values()) {
      CuentaEntity cuenta = ((CuentaEntity) object);
      if (cuenta.getTitular().equals(dni)) {
        cuentasDelCliente.add(cuenta.toCuenta());
      }
    }
    return cuentasDelCliente;
  }
}
