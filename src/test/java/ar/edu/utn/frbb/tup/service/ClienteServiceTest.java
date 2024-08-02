package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.controller.ClienteCuentasResponseDto;
import ar.edu.utn.frbb.tup.controller.ClienteRequestDto;
import ar.edu.utn.frbb.tup.controller.ClienteResponseDto;
import ar.edu.utn.frbb.tup.controller.CuentaResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteActivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
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
  @Mock private CuentaService cuentaService;
  @Mock private ClienteServiceValidator clienteServiceValidator;
  private final long dniCliente = 12345678;

  @InjectMocks private ClienteService clienteService;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testDarDeAltaClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
    ClienteRequestDto clienteDto = createClienteRequestDto();
    Cliente cliente = createCliente();

    doThrow(ClienteAlreadyExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteNoExists(cliente);

    assertThrows(
        ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));
  }

  @Test
  public void testDarDeAltaClienteMenorDeEdadException() throws ClienteMenorDeEdadException {
    ClienteRequestDto clienteDtoMenorDeEdad = createClienteRequestDto();
    Cliente clienteMenorDeEdad = createCliente();

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

    ClienteRequestDto clienteDto = createClienteRequestDto();
    Cliente cliente = createCliente();

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
  public void testBuscarClientePorDniClienteNoExistsException() {

    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.buscarClientePorDni(dniCliente));
  }

  @Test
  public void testBuscarClientePorDniSuccess()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    ClienteResponseDto clienteResponseDto = createClienteResponseDto();
    Cliente cliente = createCliente();
    clienteResponseDto.setTipoPersona(cliente.getTipoPersona().toString());

    when(clienteDao.find(dniCliente, false)).thenReturn(cliente);

    assertEquals(clienteResponseDto, clienteService.buscarClientePorDni(dniCliente));
  }

  @Test
  public void testActualizarClienteNoExistsException() throws ClienteNoExistsException {
    ClienteRequestDto clienteDto = createClienteRequestDto();
    Cliente cliente = createCliente();

    doThrow(ClienteNoExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteExists(cliente);

    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.actualizarCliente(clienteDto));
  }

  @Test
  public void testActualizarClienteMenorDeEdadException() throws ClienteMenorDeEdadException {
    ClienteRequestDto clienteDtoMenorDeEdad = createClienteRequestDto();
    Cliente clienteMenorDeEdad = createCliente();

    doThrow(ClienteMenorDeEdadException.class)
        .when(clienteServiceValidator)
        .validateClienteMayorDeEdad(clienteMenorDeEdad);

    assertThrows(
        ClienteMenorDeEdadException.class,
        () -> clienteService.actualizarCliente(clienteDtoMenorDeEdad));
  }

  @Test
  public void testActualizarClienteClienteInactivoException() throws ClienteInactivoException {
    ClienteRequestDto clienteDto = createClienteRequestDto();
    Cliente cliente = createCliente();

    doThrow(ClienteInactivoException.class)
        .when(clienteServiceValidator)
        .validateClienteIsActivo(cliente);

    assertThrows(
        ClienteInactivoException.class, () -> clienteService.actualizarCliente(clienteDto));
  }

  @Test
  public void testActualizarClienteSuccess()
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    ClienteRequestDto clienteDto = createClienteRequestDto();
    Cliente cliente = createCliente();
    ClienteResponseDto clienteResponseDto = createClienteResponseDto();
    clienteResponseDto.setTipoPersona(cliente.getTipoPersona().toString());

    assertEquals(clienteResponseDto, clienteService.actualizarCliente(clienteDto));

    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testEliminarClienteCorruptedDataInDbException()
      throws CorruptedDataInDbException,
          CuentaNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cliente.addCuenta(cuenta);

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(CorruptedDataInDbException.class)
        .when(cuentaService)
        .eliminarCuenta(cuenta.getNumeroCuenta());

    assertThrows(
        CorruptedDataInDbException.class, () -> clienteService.eliminarCliente(dniCliente));
  }

  @Test
  public void testEliminarClienteNoExistsException() throws ClienteNoExistsException {
    assertThrows(ClienteNoExistsException.class, () -> clienteService.eliminarCliente(dniCliente));
  }

  @Test
  public void testEliminarClienteImpossibleException()
      throws CorruptedDataInDbException,
          CuentaNoExistsException,
          ImpossibleException,
          IllegalArgumentException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cliente.addCuenta(cuenta);

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(ImpossibleException.class).when(cuentaService).eliminarCuenta(cuenta.getNumeroCuenta());

    assertThrows(ImpossibleException.class, () -> clienteService.eliminarCliente(dniCliente));
  }

  @Test
  public void testEliminarClienteClienteInactivoException() throws ClienteInactivoException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(ClienteInactivoException.class)
        .when(clienteServiceValidator)
        .validateClienteIsActivo(cliente);

    assertThrows(ClienteInactivoException.class, () -> clienteService.eliminarCliente(dniCliente));
  }

  @Test
  public void testEliminarClienteCuentaNoExistsSuccess()
      throws CorruptedDataInDbException,
          ClienteNoExistsException,
          CuentaNoExistsException,
          ImpossibleException,
          IllegalArgumentException,
          ClienteInactivoException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cliente.addCuenta(cuenta);
    ClienteResponseDto clienteResponseDto = createClienteResponseDto();
    clienteResponseDto.setTipoPersona(cliente.getTipoPersona().toString());
    clienteResponseDto.setActivo(false);

    Cliente clienteRes = createCliente();
    clienteRes.setActivo(false);
    clienteRes.addCuenta(cuenta);

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(CuentaNoExistsException.class)
        .when(cuentaService)
        .eliminarCuenta(cuenta.getNumeroCuenta());

    assertEquals(clienteResponseDto, clienteService.eliminarCliente(dniCliente));
    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testEliminarClienteSuccess()
      throws CorruptedDataInDbException,
          ClienteNoExistsException,
          ImpossibleException,
          IllegalArgumentException,
          ClienteInactivoException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    clienteService.eliminarCliente(dniCliente);

    assertEquals(false, cliente.isActivo());
    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testEliminarClienteYCuentaSuccess()
      throws CorruptedDataInDbException,
          ClienteNoExistsException,
          CuentaNoExistsException,
          ImpossibleException,
          IllegalArgumentException,
          ClienteInactivoException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cliente.addCuenta(cuenta);
    ClienteResponseDto clienteResponseDto = createClienteResponseDto();
    clienteResponseDto.setTipoPersona(cliente.getTipoPersona().toString());
    clienteResponseDto.setActivo(false);

    Cliente clienteRes = createCliente();
    clienteRes.setActivo(false);
    clienteRes.addCuenta(cuenta);

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    assertEquals(clienteResponseDto, clienteService.eliminarCliente(dniCliente));
    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testActivarClienteNoExistsException() throws ClienteNoExistsException {
    assertThrows(ClienteNoExistsException.class, () -> clienteService.activarCliente(dniCliente));
  }

  @Test
  public void testActivarClienteClienteInactivoException() throws ClienteActivoException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(ClienteActivoException.class)
        .when(clienteServiceValidator)
        .validateClienteIsNotActivo(cliente);

    assertThrows(ClienteActivoException.class, () -> clienteService.activarCliente(dniCliente));
  }

  @Test
  public void testActivarClienteSuccess()
      throws ClienteNoExistsException, ClienteInactivoException, ClienteActivoException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    ClienteResponseDto clienteResponseDto = clienteService.activarCliente(dniCliente);

    assertEquals(true, cliente.isActivo());
    assertEquals(clienteResponseDto, cliente.toClienteDto());
    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testActualizarClienteProtectedNoExistsException() throws ClienteNoExistsException {
    Cliente cliente = createCliente();

    doThrow(ClienteNoExistsException.class)
        .when(clienteServiceValidator)
        .validateClienteExists(cliente);

    assertThrows(ClienteNoExistsException.class, () -> clienteService.actualizarCliente(cliente));
  }

  @Test
  public void testActualizarClienteProtectedMenorDeEdadException()
      throws ClienteMenorDeEdadException {
    Cliente clienteMenorDeEdad = createCliente();

    doThrow(ClienteMenorDeEdadException.class)
        .when(clienteServiceValidator)
        .validateClienteMayorDeEdad(clienteMenorDeEdad);

    assertThrows(
        ClienteMenorDeEdadException.class,
        () -> clienteService.actualizarCliente(clienteMenorDeEdad));
  }

  @Test
  public void testActualizarClienteProtectedClienteInactivoException()
      throws ClienteInactivoException {
    Cliente cliente = createCliente();

    doThrow(ClienteInactivoException.class)
        .when(clienteServiceValidator)
        .validateClienteIsActivo(cliente);

    assertThrows(ClienteInactivoException.class, () -> clienteService.actualizarCliente(cliente));
  }

  @Test
  public void testActualizarClienteProtectedSuccess()
      throws ClienteNoExistsException, ClienteMenorDeEdadException, ClienteInactivoException {
    Cliente cliente = createCliente();
    ClienteResponseDto clienteResponseDto = createClienteResponseDto();
    clienteResponseDto.setTipoPersona(cliente.getTipoPersona().toString());

    clienteService.actualizarCliente(cliente);

    verify(clienteDao, times(1)).save(cliente);
  }

  @Test
  public void testBuscarClienteCompletoPorDniClienteNoExistsException() {

    assertThrows(
        ClienteNoExistsException.class,
        () -> clienteService.buscarClienteCompletoPorDni(dniCliente));
  }

  @Test
  public void testBuscarClienteCompletoPorDniSuccess()
      throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
    ClienteRequestDto clienteDto = createClienteRequestDto();
    Cliente cliente = createCliente();
    clienteDto.setTipoPersona(cliente.getTipoPersona().toString());

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    assertEquals(cliente, clienteService.buscarClienteCompletoPorDni(dniCliente));
  }

  @Test
  public void testGetClienteByCuentaClienteNoExistsException() {
    assertThrows(
        ClienteNoExistsException.class, () -> clienteService.getClienteByCuenta(dniCliente));
  }

  @Test
  public void testGetClienteByCuentaSucess() throws ClienteNoExistsException {
    Cliente cliente = createCliente();

    when(clienteDao.getClienteByCuenta(dniCliente)).thenReturn(cliente);

    assertEquals(cliente, clienteService.getClienteByCuenta(dniCliente));
  }

  @Test
  public void testBuscarCuentasDeClienteByDniClienteNoExistsException() {
    assertThrows(
        ClienteNoExistsException.class,
        () -> clienteService.buscarCuentasDeClientePorDni(dniCliente));
  }

  @Test
  public void testBuscarCuentasDeClienteByDniClienteInactivoException()
      throws ClienteInactivoException {
    Cliente cliente = createCliente();

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    doThrow(ClienteInactivoException.class)
        .when(clienteServiceValidator)
        .validateClienteIsActivo(cliente);

    assertThrows(
        ClienteInactivoException.class,
        () -> clienteService.buscarCuentasDeClientePorDni(dniCliente));
  }

  @Test
  public void testBuscarCuentasDeClienteByDniSuccess()
      throws ClienteNoExistsException, ClienteInactivoException {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cliente.addCuenta(cuenta);

    ClienteCuentasResponseDto clienteCuentasResponseDtoExpected =
        cliente.toClienteCuentasResponseDto();
    CuentaResponseDto cuentaResponseDto = cuenta.toCuentaResponseDto();
    clienteCuentasResponseDtoExpected.addCuenta(cuentaResponseDto);

    when(clienteDao.find(dniCliente, true)).thenReturn(cliente);

    ClienteCuentasResponseDto clienteCuentasResponseDto =
        clienteService.buscarCuentasDeClientePorDni(dniCliente);

    assertEquals(clienteCuentasResponseDtoExpected, clienteCuentasResponseDto);
  }

  private ClienteRequestDto createClienteRequestDto() {
    ClienteRequestDto clienteDto = new ClienteRequestDto();
    clienteDto.setDni(dniCliente);
    clienteDto.setNombre("Nombre");
    clienteDto.setApellido("Apellido");
    clienteDto.setFechaNacimiento("1990-01-01");
    clienteDto.setTipoPersona("F");
    return clienteDto;
  }

  private ClienteResponseDto createClienteResponseDto() {
    ClienteResponseDto clienteDto = new ClienteResponseDto();
    clienteDto.setDni(dniCliente);
    clienteDto.setNombre("Nombre");
    clienteDto.setApellido("Apellido");
    clienteDto.setFechaNacimiento("1990-01-01");
    clienteDto.setTipoPersona("F");
    clienteDto.setActivo(true);
    return clienteDto;
  }

  private Cliente createCliente() {
    ClienteRequestDto clienteDto = createClienteRequestDto();
    return new Cliente(clienteDto);
  }

  private Cuenta createCuenta() {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1);
    cuenta.setBalance(500000);
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cuenta.setMoneda(TipoMoneda.PESOS_ARGENTINOS);
    cuenta.setActivo(true);
    return cuenta;
  }
}
