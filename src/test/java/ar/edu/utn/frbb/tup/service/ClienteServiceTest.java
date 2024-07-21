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
  public void testDarDeAltaClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
    ClienteDto clienteDto = createClienteDto();
    Cliente cliente = new Cliente(clienteDto);

    doThrow(ClienteAlreadyExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteNoExists(cliente);

    assertThrows(
        ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));
  }

  @Test
  public void testDarDeAltaClienteMenorDeEdadException() throws ClienteMenorDeEdadException {
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
  public void testAgregarCuentaClienteNoExistsException()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {

    when(clienteDao.find(dniCliente, true)).thenReturn(null);

    Cuenta cuenta = createCuenta();

    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.agregarCuenta(cuenta, dniCliente));
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
  public void testObtenerTitularConCuentaClienteNoExistsException() {

    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(null);

    assertThrows(
        ClienteNoExistsException.class,
        () -> clienteService.obtenerTitularConCuenta(cuenta, dniCliente));
  }

  @Test
  public void testObtenerTitularConCuentaTipoCuentaAlreadyExistsException()
      throws TipoCuentaAlreadyExistsException {

    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);
    doThrow(TipoCuentaAlreadyExistsException.class)
        .when(clienteServiceValidator)
        .validateTipoCuentaUnica(cliente, cuenta);

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> clienteService.obtenerTitularConCuenta(cuenta, dniCliente));
  }

  @Test
  public void testObtenerTitularConCuentaSuccess()
      throws ClienteNoExistsException, TipoCuentaAlreadyExistsException {

    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    Cliente clienteResult = clienteService.obtenerTitularConCuenta(cuenta, cliente.getDni());
    assertEquals(cuenta, clienteResult.getCuentaById(cuenta.getNumeroCuenta()));
    assertEquals(cliente.getDni(), clienteResult.getDni());
    assertEquals(cliente.getEdad(), clienteResult.getEdad());
    assertEquals(cliente.getNombre(), clienteResult.getNombre());
    assertEquals(cliente.getApellido(), clienteResult.getApellido());
    assertEquals(cliente.getTipoPersona(), clienteResult.getTipoPersona());
    assertEquals(cliente.getBanco(), clienteResult.getBanco());
  }

  @Test
  public void testBuscarClientePorDniClienteNoExistsException() {

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
  public void testActualizarClienteNoExistsOnUpdateException() throws ClienteNoExistsException {
    ClienteDto clienteDto = createClienteDto();
    Cliente cliente = new Cliente(clienteDto);

    doThrow(ClienteNoExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteExists(cliente);

    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.actualizarCliente(clienteDto));
  }

  @Test
  public void testActualizarClienteMenorDeEdadOnUpdateException()
      throws ClienteMenorDeEdadException {
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
  public void testEliminarClienteNoExistsException() throws ClienteNoExistsException {
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

  @Test
  public void testActivarClienteNoExistsException() throws ClienteNoExistsException {
    assertThrows(ClienteNoExistsException.class, () -> clienteService.activarCliente(dniCliente));
  }

  @Test
  public void testActivarClienteSuccess() throws ClienteNoExistsException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    clienteService.activarCliente(dniCliente);

    assertEquals(true, cliente.isActivo());
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
        .setNumeroCuenta(1)
        .setMoneda(TipoMoneda.PESOS_ARGENTINOS)
        .setBalance(500000)
        .setTipoCuenta(TipoCuenta.CAJA_AHORROS);
  }
}
