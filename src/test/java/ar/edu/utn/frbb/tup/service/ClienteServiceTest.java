package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.validator.ClienteServiceValidator;
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
  @Mock private ClienteServiceValidator clienteServiceValidator;
  private final long dniCliente = 12345678;

  @InjectMocks private ClienteService clienteService;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
    ClienteDto clienteDto = createClienteDto();
    Cliente cliente = new Cliente(clienteDto);

    doThrow(ClienteAlreadyExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteNoExists(cliente);

    assertThrows(
        ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));
  }

  @Test
  public void testClienteMenorDeEdadException() throws ClienteMenorDeEdadException {
    ClienteDto clienteDtoMenorDeEdad = createClienteDto();
    Cliente clienteMenorDeEdad = new Cliente(clienteDtoMenorDeEdad);

    doThrow(ClienteMenorDeEdadException.class)
        .when(clienteServiceValidator)
        .validateClienteMayorDeEdad(clienteMenorDeEdad);

    assertThrows(
        ClienteMenorDeEdadException.class,
        () -> clienteService.darDeAltaCliente(clienteDtoMenorDeEdad));
  }

  @Test
  public void testDarDeAltaClienteSuccess()
      throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {

    ClienteDto clienteDto = createClienteDto();
    Cliente cliente = new Cliente(clienteDto);

    clienteService.darDeAltaCliente(clienteDto);

    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testAgregarCuentaTipoCuentaAlreadExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {

    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(clienteServiceValidator)
        .validateTipoCuentaUnica(cliente, cuenta);

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> clienteService.agregarCuenta(cuenta, dniCliente));
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

    Cliente cliente = createCliente();

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

    Cliente cliente = createCliente();

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
  public void testBuscarClientePorDniClienteNoExistsException() throws ClienteNoExistsException {

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

  @Test
  public void testClienteNoExistsOnUpdateException() throws ClienteNoExistsException {
    ClienteDto clienteDto = createClienteDto();
    Cliente cliente = new Cliente(clienteDto);

    doThrow(ClienteNoExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteExists(cliente);

    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.actualizarCliente(clienteDto));
  }

  @Test
  public void testClienteMenorDeEdadOnUpdateException() throws ClienteMenorDeEdadException {
    ClienteDto clienteDtoMenorDeEdad = createClienteDto();
    Cliente clienteMenorDeEdad = new Cliente(clienteDtoMenorDeEdad);

    doThrow(ClienteMenorDeEdadException.class)
        .when(clienteServiceValidator)
        .validateClienteMayorDeEdad(clienteMenorDeEdad);

    assertThrows(
        ClienteMenorDeEdadException.class,
        () -> clienteService.actualizarCliente(clienteDtoMenorDeEdad));
  }

  @Test
  public void testActualizarClienteSuccess()
      throws ClienteNoExistsException, ClienteMenorDeEdadException {
    ClienteDto clienteDto = createClienteDto();
    Cliente cliente = new Cliente(clienteDto);

    assertEquals(cliente, clienteService.actualizarCliente(clienteDto));

    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testClienteNoExistsOnDeleteException() throws ClienteNoExistsException {
    assertThrows(ClienteNoExistsException.class, () -> clienteService.eliminarCliente(dniCliente));
  }

  @Test
  public void testEliminarClienteSuccess() throws ClienteNoExistsException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    clienteService.eliminarCliente(dniCliente);

    assertEquals(false, cliente.isActivo());
    verify(clienteDao, times(1)).save(cliente);
  }

  private ClienteDto createClienteDto() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setDni(dniCliente);
    clienteDto.setNombre("Nombre");
    clienteDto.setApellido("Apellido");
    clienteDto.setFechaNacimiento("1990-01-01");
    clienteDto.setTipoPersona("F");
    return clienteDto;
  }

  private Cliente createCliente() {
    ClienteDto clienteDto = createClienteDto();
    return new Cliente(clienteDto);
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
