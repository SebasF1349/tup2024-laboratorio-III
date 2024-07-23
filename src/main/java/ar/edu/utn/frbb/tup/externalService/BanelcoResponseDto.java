package ar.edu.utn.frbb.tup.externalService;

public class BanelcoResponseDto {
  private int statusCode;
  private int internalCode;
  private String message;

  public BanelcoResponseDto() {}

  public BanelcoResponseDto(int statusCode, int internalCode, String message) {
    this.statusCode = statusCode;
    this.internalCode = internalCode;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public int getInternalCode() {
    return internalCode;
  }

  public void setInternalCode(int internalCode) {
    this.internalCode = internalCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + statusCode;
    result = prime * result + internalCode;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BanelcoResponseDto other = (BanelcoResponseDto) obj;
    if (statusCode != other.statusCode) {
      return false;
    }
    if (internalCode != other.internalCode) {
      return false;
    }
    if (message == null) {
      if (other.message != null) {
        return false;
      }
    } else if (!message.equals(other.message)) {
      return false;
    }
    return true;
  }
}
