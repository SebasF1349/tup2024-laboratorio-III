package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import org.springframework.stereotype.Service;

@Service
public class MovimientoDao extends AbstractBaseDao {
  @Override
  protected String getEntityName() {
    return "MOVIMIENTO";
  }

  public Movimiento find(String idMovimiento) {
    if (getInMemoryDatabase().get(idMovimiento) == null) return null;
    return ((MovimientoEntity) getInMemoryDatabase().get(idMovimiento)).toMovimiento();
  }

  public void save(Movimiento movimiento) {
    MovimientoEntity entity = new MovimientoEntity(movimiento);
    getInMemoryDatabase().put(entity.getId(), entity);
  }
}
