package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;

public class CuentaProcessor extends BusinessProcessor {
  @Autowired CuentaService cuentaService = new CuentaService();
}
