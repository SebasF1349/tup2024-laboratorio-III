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
import ar.edu.utn.frbb.tup.model.Cuenta;
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
  private final long clienteDni = 12345678;
  private final String endpoint = "/cuenta";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @InjectMocks private CuentaController cuentaController;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testObtenerCuentaDoesntExistsFail() throws Exception {
    doThrow(new CuentaNoExistsException("")).when(cuentaService).buscarCuentaPorId(clienteDni);

    mockMvc
        .perform(get(createEndpoint(clienteDni)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testObtenerCuentaSuccess() throws Exception {
    Cuenta cuenta = createCuenta();
    String cuentaMapped = objectMapper.writeValueAsString(cuenta);

    Mockito.when(cuentaService.buscarCuentaPorId(clienteDni)).thenReturn(cuenta);

    mockMvc
        .perform(get(createEndpoint(clienteDni)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaMapped));
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
    doThrow(new CuentaNoExistsException("")).when(cuentaService).eliminarCuenta(clienteDni);

    mockMvc
        .perform(delete(createEndpoint(clienteDni)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testEliminarCuentaCorruptedDataInDbException() throws Exception {
    doThrow(new CorruptedDataInDbException("")).when(cuentaService).eliminarCuenta(clienteDni);

    mockMvc
        .perform(delete(createEndpoint(clienteDni)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testEliminarCuentaSuccess() throws Exception {
    Cuenta cuenta = createCuenta();
    String cuentaMapped = objectMapper.writeValueAsString(cuenta);

    Mockito.when(cuentaService.eliminarCuenta(clienteDni)).thenReturn(cuenta);

    mockMvc
        .perform(delete(createEndpoint(clienteDni)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaMapped));
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
    Cuenta cuenta = createCuenta();
    String cuentaMapped = objectMapper.writeValueAsString(cuenta);

    when(cuentaService.actualizarCuenta(cuentaDto)).thenReturn(cuenta);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaMapped));
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
    cuentaDto.setTitular(clienteDni);
    return cuentaDto;
  }

  private Cuenta createCuenta() {
    CuentaDto cuentaDto = createCuentaDto();
    return new Cuenta(cuentaDto);
  }
}
