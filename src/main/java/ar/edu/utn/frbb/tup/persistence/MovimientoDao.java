package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovimientoDao extends AbstractBaseDao {
  @Override
  protected String getEntityName() {
    return "MOVIMIENTO";
  }

  public Movimiento find(long idMovimiento) {
    if (getInMemoryDatabase().get(idMovimiento) == null) return null;
    return ((MovimientoEntity) getInMemoryDatabase().get(idMovimiento)).toMovimiento();
  }

  public void save(Movimiento movimiento) {
    MovimientoEntity entity = new MovimientoEntity(movimiento);
    getInMemoryDatabase().put(entity.getId(), entity);
  }

  public List<Movimiento> getMovimientosByCuenta(long numeroCuenta) {
    List<Movimiento> movimientosDeCuenta = new ArrayList<>();
    for (Object object : getInMemoryDatabase().values()) {
      MovimientoEntity movimiento = ((MovimientoEntity) object);
      if (movimiento.getNumeroCuenta() == numeroCuenta
          || movimiento.getNumeroCuentaDestino() == numeroCuenta) {
        movimientosDeCuenta.add(movimiento.toMovimiento());
      }
    }
    return movimientosDeCuenta;
  }
}
