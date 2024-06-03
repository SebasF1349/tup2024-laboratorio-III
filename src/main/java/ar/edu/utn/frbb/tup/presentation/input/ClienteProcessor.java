package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;

public class ClienteProcessor extends BusinessProcessor {
  @Autowired ClienteService clienteService = new ClienteService();
}
