package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

  @Mock private ClienteDao clienteDao;
  private final String dniCliente = "12345678";

  @InjectMocks private ClienteService clienteService;

  private Cliente createCliente() {
    Cliente cliente = new Cliente();
    cliente.setDni(dniCliente);
    cliente.setNombre("Nombre");
    cliente.setApellido("Apellido");
    cliente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
    cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
    return cliente;
  }

  private Cuenta createCuenta() {
    return new Cuenta()
        .setMoneda(TipoMoneda.PESOS_ARGENTINOS)
        .setBalance(500000)
        .setTipoCuenta(TipoCuenta.CAJA_AHORROS);
  }

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testClienteMenor18Años() {
    Cliente clienteMenorDeEdad = new Cliente();
    clienteMenorDeEdad.setFechaNacimiento(LocalDate.now().minusYears(17));
    assertThrows(
        IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
  }

  @Test
  public void testClienteSuccess() throws ClienteAlreadyExistsException {
    Cliente cliente = createCliente();
    clienteService.darDeAltaCliente(cliente);

    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, false)).thenReturn(cliente);

    assertThrows(
        ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(cliente));
  }

  @Test
  public void testAgregarCuentaAClienteSuccess()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    clienteService.agregarCuenta(cuenta, cliente.getDni());

    verify(clienteDao, times(1)).save(cliente);

    assertEquals(1, cliente.getCuentas().size());
    assertEquals(cliente, cuenta.getTitular());
  }

  @Test
  public void testAgregarCuentaAClienteDuplicada()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    clienteService.agregarCuenta(cuenta, cliente.getDni());

    Cuenta cuenta2 = createCuenta();

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> clienteService.agregarCuenta(cuenta2, cliente.getDni()));
    verify(clienteDao, times(1)).save(cliente);
    assertEquals(1, cliente.getCuentas().size());
    assertEquals(cliente, cuenta.getTitular());
  }

  // Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
  // Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino...
  // Testear clienteService.buscarPorDni
}
