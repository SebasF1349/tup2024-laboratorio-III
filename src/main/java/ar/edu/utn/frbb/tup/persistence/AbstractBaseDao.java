package ar.edu.utn.frbb.tup.persistence;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseDao {
  protected static Map<String, Map<String, Object>> poorMansDatabase = new HashMap<>();

  protected abstract String getEntityName();

  protected Map<String, Object> getInMemoryDatabase() {
    if (poorMansDatabase.get(getEntityName()) == null) {
      poorMansDatabase.put(getEntityName(), new HashMap<>());
    }
    return poorMansDatabase.get(getEntityName());
  }
}
