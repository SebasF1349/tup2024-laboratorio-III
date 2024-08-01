package ar.edu.utn.frbb.tup.service.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteActivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
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
public class ClienteServiceValidatorTest {

  @Mock private ClienteDao clienteDao;

  @InjectMocks private ClienteServiceValidator clienteServiceValidator;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void validateClienteNoExistsFail() {
    Cliente cliente = createCliente();

    when(clienteDao.find(cliente.getDni(), false)).thenReturn(cliente);

    assertThrows(
        ClienteAlreadyExistsException.class,
        () -> clienteServiceValidator.validateClienteNoExists(cliente));
  }

  @Test
  public void validateClienteNoExistsSuccess() {
    Cliente cliente = createCliente();

    when(clienteDao.find(cliente.getDni(), false)).thenReturn(null);

    assertDoesNotThrow(() -> clienteServiceValidator.validateClienteNoExists(cliente));
  }

  @Test
  public void validateClienteExistsFail() {
    Cliente cliente = createCliente();

    when(clienteDao.find(cliente.getDni(), false)).thenReturn(null);

    assertThrows(
        ClienteNoExistsException.class,
        () -> clienteServiceValidator.validateClienteExists(cliente));
  }

  @Test
  public void validateClienteExistsSuccess() {
    Cliente cliente = createCliente();

    when(clienteDao.find(cliente.getDni(), false)).thenReturn(cliente);

    assertDoesNotThrow(() -> clienteServiceValidator.validateClienteExists(cliente));
  }

  @Test
  public void testValidateClienteMayorDeEdadFail() {
    Cliente cliente = createCliente();
    cliente.setFechaNacimiento(LocalDate.now().minusYears(17));

    assertThrows(
        ClienteMenorDeEdadException.class,
        () -> clienteServiceValidator.validateClienteMayorDeEdad(cliente));
  }

  @Test
  public void testValidateClienteMayorDeEdadSuccess() {
    Cliente cliente = createCliente();
    cliente.setFechaNacimiento(LocalDate.now().minusYears(19));

    assertDoesNotThrow(() -> clienteServiceValidator.validateClienteMayorDeEdad(cliente));
  }

  @Test
  public void testValidateTipoCuentaUnicaFail() {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cliente.addCuenta(cuenta);

    assertThrows(
        TipoCuentaAlreadyExistsException.class,
        () -> clienteServiceValidator.validateTipoCuentaUnica(cliente, cuenta));
  }

  @Test
  public void testValidateTipoCuentaUnicaSuccess() {
    Cliente cliente = createCliente();
    Cuenta cuenta = createCuenta();
    cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORROS);
    cliente.addCuenta(cuenta);

    Cuenta cuentaDos = createCuenta();
    cuentaDos.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

    assertDoesNotThrow(() -> clienteServiceValidator.validateTipoCuentaUnica(cliente, cuentaDos));
  }

  @Test
  public void testValidateClienteIsActivoClienteInactivoException() {
    Cliente cliente = createCliente();
    cliente.setActivo(false);

    assertThrows(
        ClienteInactivoException.class,
        () -> clienteServiceValidator.validateClienteIsActivo(cliente));
  }

  @Test
  public void testValidateClienteIsActivoSuccess() {
    Cliente cliente = createCliente();
    cliente.setActivo(true);

    assertDoesNotThrow(() -> clienteServiceValidator.validateClienteIsActivo(cliente));
  }

  @Test
  public void testValidateClienteIsNotActivoClienteActivoException() {
    Cliente cliente = createCliente();
    cliente.setActivo(true);

    assertThrows(
        ClienteActivoException.class,
        () -> clienteServiceValidator.validateClienteIsNotActivo(cliente));
  }

  @Test
  public void testValidateClienteIsNotActivoSuccess() {
    Cliente cliente = createCliente();
    cliente.setActivo(false);

    assertDoesNotThrow(() -> clienteServiceValidator.validateClienteIsNotActivo(cliente));
  }

  private Cliente createCliente() {
    Cliente cliente = new Cliente();
    cliente.setDni(123456);
    cliente.setNombre("Nombre");
    cliente.setApellido("Apellido");
    return cliente;
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
