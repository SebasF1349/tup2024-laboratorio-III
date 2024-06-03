package ar.edu.utn.frbb.tup.persistence.entity;

public class BaseEntity {
  private final String Id;

  public BaseEntity(String id) {
    Id = id;
  }

  public String getId() {
    return Id;
  }
}
