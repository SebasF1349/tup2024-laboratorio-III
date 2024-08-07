package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovimientoDao extends AbstractBaseDao {

  public Movimiento find(long idMovimiento) throws ImpossibleException {
    if (getInMemoryDatabase().get(idMovimiento) == null) return null;
    return ((MovimientoEntity) getInMemoryDatabase().get(idMovimiento)).toMovimiento();
  }

  public void save(Movimiento movimiento) {
    if (movimiento.getMovimientoId() == 0) {
      movimiento.setMovimientoId(getInMemoryDatabase().size() + 1);
    }
    MovimientoEntity entity = new MovimientoEntity(movimiento);
    getInMemoryDatabase().put(entity.getId(), entity);
  }

  public List<Movimiento> getMovimientosByCuenta(long numeroCuenta) throws ImpossibleException {
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

  @Override
  protected String getEntityName() {
    return "MOVIMIENTO";
  }
}
