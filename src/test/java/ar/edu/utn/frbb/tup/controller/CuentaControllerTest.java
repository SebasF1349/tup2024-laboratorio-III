package ar.edu.utn.frbb.tup.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frbb.tup.controller.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(CuentaController.class)
public class CuentaControllerTest {

  @MockBean private CuentaService cuentaService;
  @MockBean private CuentaControllerValidator cuentaControllerValidator;
  private final long numeroCuenta = 1;
  private final String endpoint = "/api/cuenta";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @InjectMocks private CuentaController cuentaController;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testObtenerCuentaDoesntExistsFail() throws Exception {
    doThrow(new CuentaNoExistsException("")).when(cuentaService).buscarCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testObtenerCuentaCorruptedDataInDbException() throws Exception {
    doThrow(new CorruptedDataInDbException("")).when(cuentaService).buscarCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testObtenerCuentaSuccess() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    Mockito.when(cuentaService.buscarCuentaPorId(numeroCuenta)).thenReturn(cuentaDto);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaDtoMapped));
  }

  @Test
  public void testCrearCuentaWrongInputDataFail() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new WrongInputDataException("")).when(cuentaControllerValidator).validate(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testCrearCuentaNoSoportadaException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new CuentaNoSoportadaException("")).when(cuentaService).darDeAltaCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400121)));
  }

  @Test
  public void testCrearCuentaTipoCuentaAlreadyExistsException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new TipoCuentaAlreadyExistsException(""))
        .when(cuentaService)
        .darDeAltaCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400123)));
  }

  @Test
  public void testCrearCuentaClienteNoExistsException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new CorruptedDataInDbException("")).when(cuentaService).darDeAltaCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testCrearCuentaCorruptedDataInDBException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new ClienteNoExistsException("")).when(cuentaService).darDeAltaCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404101)));
  }

  @Test
  public void testCrearCuentaSuccess() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isCreated())
        .andExpect(content().string(cuentaDtoMapped));
  }

  @Test
  public void testEliminarCuentaDoesntExistsFail() throws Exception {
    doThrow(new CuentaNoExistsException("")).when(cuentaService).eliminarCuenta(numeroCuenta);

    mockMvc
        .perform(delete(createEndpoint(numeroCuenta)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testEliminarCuentaCorruptedDataInDbException() throws Exception {
    doThrow(new CorruptedDataInDbException("")).when(cuentaService).eliminarCuenta(numeroCuenta);

    mockMvc
        .perform(delete(createEndpoint(numeroCuenta)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testEliminarCuentaSuccess() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    Mockito.when(cuentaService.eliminarCuenta(numeroCuenta)).thenReturn(cuentaDto);

    mockMvc
        .perform(delete(createEndpoint(numeroCuenta)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaDtoMapped));
  }

  @Test
  public void testActualizarCuentaWrongInputDataException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new WrongInputDataException("")).when(cuentaControllerValidator).validate(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testActualizarCuentaNoExistsFail() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new CuentaNoExistsException("")).when(cuentaService).actualizarCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testActualizarCuentaClienteNoExistsException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new ClienteNoExistsException("")).when(cuentaService).actualizarCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404101)));
  }

  @Test
  public void testActualizarCuentaNoSoportadaException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new CuentaNoSoportadaException("")).when(cuentaService).actualizarCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400121)));
  }

  @Test
  public void testActualizarCuentaTipoCuentaAlreadyExistsException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new TipoCuentaAlreadyExistsException(""))
        .when(cuentaService)
        .actualizarCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400123)));
  }

  @Test
  public void testActualizarCuentaCorruptedDataInDbException() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new CorruptedDataInDbException("")).when(cuentaService).actualizarCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testActualizarCuentaSuccess() throws Exception {
    CuentaDto cuentaDto = createCuentaDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    when(cuentaService.actualizarCuenta(cuentaDto)).thenReturn(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaDtoMapped));
  }

  @Test
  public void testObtenerTransaccionesEnCuentaDoesntExistsFail() throws Exception {
    doThrow(new CuentaNoExistsException(""))
        .when(cuentaService)
        .buscarTransaccionesDeCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta) + "/transacciones"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testObtenerTransaccionesEnCuentaCorruptedDataInDbException() throws Exception {
    doThrow(new CorruptedDataInDbException(""))
        .when(cuentaService)
        .buscarTransaccionesDeCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta) + "/transacciones"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testObtenerTransaccionesEnCuentaSuccess() throws Exception {
    CuentaMovimientosResponseDto cuentaMovimientosResponseDto = new CuentaMovimientosResponseDto();
    String cuentaMovimientosResponseDtoMapped =
        objectMapper.writeValueAsString(cuentaMovimientosResponseDto);

    Mockito.when(cuentaService.buscarTransaccionesDeCuentaPorId(numeroCuenta))
        .thenReturn(cuentaMovimientosResponseDto);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta) + "/transacciones"))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaMovimientosResponseDtoMapped));
  }

  private String createEndpoint(long end) {
    return endpoint + "/" + end;
  }

  private String getEndpoint() {
    return endpoint;
  }

  private CuentaDto createCuentaDto() {
    CuentaDto cuentaDto = new CuentaDto();
    cuentaDto.setBalance(500000);
    cuentaDto.setMoneda("P");
    cuentaDto.setTipoCuenta("A");
    cuentaDto.setTitular(12345678);
    return cuentaDto;
  }
}
