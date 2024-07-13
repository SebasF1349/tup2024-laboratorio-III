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

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, false)).thenReturn(cliente);

    assertThrows(
        ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(cliente));
  }

  @Test
  public void testClienteMenorDeEdadException() {
    Cliente clienteMenorDeEdad = new Cliente();
    clienteMenorDeEdad.setFechaNacimiento(LocalDate.now().minusYears(17));
    assertThrows(
        ClienteMenorDeEdadException.class,
        () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
  }

  @Test
  public void testDarDeAltaClienteSuccess()
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
    Cliente cliente = createCliente();
    clienteService.darDeAltaCliente(cliente);

    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testTipoCuentaAlreadExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    clienteService.agregarCuenta(cuenta, cliente.getDni());

    verify(clienteDao, times(1)).save(cliente);
    assertEquals(1, cliente.getCuentas().size());
    assertEquals(cliente, cuenta.getTitular());

    Cuenta cuentaDuplicada = createCuenta();

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> clienteService.agregarCuenta(cuentaDuplicada, cliente.getDni()));
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
  public void testAgregarDosCuentaDistintoTipoSuccess()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente cliente = this.createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    Cuenta cuentaAhorro = this.createCuenta(TipoMoneda.PESOS_ARGENTINOS, TipoCuenta.CAJA_AHORROS);
    clienteService.agregarCuenta(cuentaAhorro, cliente.getDni());

    Cuenta cuentaCorriente =
        this.createCuenta(TipoMoneda.PESOS_ARGENTINOS, TipoCuenta.CUENTA_CORRIENTE);
    clienteService.agregarCuenta(cuentaCorriente, cliente.getDni());

    verify(clienteDao, times(2)).save(cliente);
    assertEquals(2, cliente.getCuentas().size());
    assertEquals(cliente, cuentaAhorro.getTitular());
    assertEquals(cliente, cuentaCorriente.getTitular());
  }

  @Test
  public void testAgregarDosCuentaDistintaMonedaSuccess()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente cliente = this.createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    Cuenta cuentaPesos = this.createCuenta(TipoMoneda.PESOS_ARGENTINOS, TipoCuenta.CAJA_AHORROS);
    clienteService.agregarCuenta(cuentaPesos, cliente.getDni());

    Cuenta cuentaDolares =
        this.createCuenta(TipoMoneda.DOLARES_AMERICANOS, TipoCuenta.CAJA_AHORROS);
    clienteService.agregarCuenta(cuentaDolares, cliente.getDni());

    verify(clienteDao, times(2)).save(cliente);
    assertEquals(2, cliente.getCuentas().size());
    assertEquals(cliente, cuentaPesos.getTitular());
    assertEquals(cliente, cuentaDolares.getTitular());
  }

  @Test
  public void testClienteNoExisteException() throws TipoCuentaAlreadyExistsException {
    when(clienteDao.find(dniCliente, true)).thenReturn(null);

    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.buscarClientePorDni(dniCliente));
  }

  @Test
  public void testBuscarClientePorDniSuccess()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    assertEquals(cliente, clienteService.buscarClientePorDni(dniCliente));
  }

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

  private Cuenta createCuenta(TipoMoneda moneda, TipoCuenta cuenta) {
    return new Cuenta().setMoneda(moneda).setBalance(500000).setTipoCuenta(cuenta);
  }
}
