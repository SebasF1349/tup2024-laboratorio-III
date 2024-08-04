package ar.edu.utn.frbb.tup.controller;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frbb.tup.controller.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteInactivoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaActivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaInactivaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
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
  public void testObtenerCuentaDoesntExistsException() throws Exception {
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
  public void testObtenerCuentaImpossibleException() throws Exception {
    doThrow(new ImpossibleException("")).when(cuentaService).buscarCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testObtenerCuentaSuccess() throws Exception {
    CuentaResponseDto cuentaDto = createCuentaResponseDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    Mockito.when(cuentaService.buscarCuentaPorId(numeroCuenta)).thenReturn(cuentaDto);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaDtoMapped));
  }

  @Test
  public void testCrearCuentaWithoutData() throws Exception {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400104)))
        .andExpect(jsonPath("$.errors", aMapWithSize(3)))
        .andExpect(jsonPath("$.errors.tipoCuenta", is("must not be null")))
        .andExpect(jsonPath("$.errors.moneda", is("must not be null")))
        .andExpect(jsonPath("$.errors.titular", is("must be greater than 0")));
  }

  @Test
  public void testCrearCuentaWithInvalidNumberData() throws Exception {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setBalance(-1000);
    cuentaDto.setTitular(0);
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400104)))
        .andExpect(jsonPath("$.errors", aMapWithSize(4)))
        .andExpect(jsonPath("$.errors.balance", is("must be greater than or equal to 0")))
        .andExpect(jsonPath("$.errors.tipoCuenta", is("must not be null")))
        .andExpect(jsonPath("$.errors.moneda", is("must not be null")))
        .andExpect(jsonPath("$.errors.titular", is("must be greater than 0")));
  }

  @Test
  public void testCrearCuentaWrongInputDataException() throws Exception {
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
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
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
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
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
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
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
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
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
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
  public void testCrearCuentaClienteInactivoException() throws Exception {
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new ClienteInactivoException("")).when(cuentaService).darDeAltaCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400112)));
  }

  @Test
  public void testCrearCuentaInactivaException() throws Exception {
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);

    doThrow(new CuentaInactivaException("")).when(cuentaService).darDeAltaCuenta(cuentaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400114)));
  }

  @Test
  public void testCrearCuentaSuccess() throws Exception {
    CuentaRequestDto cuentaDto = createCuentaRequestDto();
    String cuentaDtoMapped = objectMapper.writeValueAsString(cuentaDto);
    CuentaResponseDto cuentaResponseDto = createCuentaResponseDto();
    String cuentaResponseDtoMapped = objectMapper.writeValueAsString(cuentaResponseDto);

    when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuentaResponseDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(cuentaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isCreated())
        .andExpect(content().string(cuentaResponseDtoMapped));
  }

  @Test
  public void testEliminarCuentaDoesntExistsException() throws Exception {
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
  public void testEliminarCuentaImpossibleException() throws Exception {
    doThrow(new ImpossibleException("")).when(cuentaService).eliminarCuenta(numeroCuenta);

    mockMvc
        .perform(delete(createEndpoint(numeroCuenta)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testEliminarCuentaInactivaException() throws Exception {
    doThrow(new CuentaInactivaException("")).when(cuentaService).eliminarCuenta(numeroCuenta);

    mockMvc
        .perform(delete(createEndpoint(numeroCuenta)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400114)));
  }

  @Test
  public void testEliminarCuentaSuccess() throws Exception {
    CuentaResponseDto cuentaResponseDto = createCuentaResponseDto();
    String cuentaResponseDtoMapped = objectMapper.writeValueAsString(cuentaResponseDto);

    Mockito.when(cuentaService.eliminarCuenta(numeroCuenta)).thenReturn(cuentaResponseDto);

    mockMvc
        .perform(delete(createEndpoint(numeroCuenta)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaResponseDtoMapped));
  }

  @Test
  public void testActivarCuentaDoesntExistsException() throws Exception {
    doThrow(new CuentaNoExistsException("")).when(cuentaService).activarCuenta(numeroCuenta);

    mockMvc
        .perform(patch(createEndpoint(numeroCuenta)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testActivarCuentaCorruptedDataInDbException() throws Exception {
    doThrow(new CorruptedDataInDbException("")).when(cuentaService).activarCuenta(numeroCuenta);

    mockMvc
        .perform(patch(createEndpoint(numeroCuenta)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testActivarCuentaImpossibleException() throws Exception {
    doThrow(new ImpossibleException("")).when(cuentaService).activarCuenta(numeroCuenta);

    mockMvc
        .perform(patch(createEndpoint(numeroCuenta)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testActivarCuentaActivaException() throws Exception {
    doThrow(new CuentaActivaException("")).when(cuentaService).activarCuenta(numeroCuenta);

    mockMvc
        .perform(patch(createEndpoint(numeroCuenta)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400115)));
  }

  @Test
  public void testActivarCuentaSuccess() throws Exception {
    CuentaResponseDto cuentaResponseDto = createCuentaResponseDto();
    String cuentaMovimientosResponseDtoMapped = objectMapper.writeValueAsString(cuentaResponseDto);

    Mockito.when(cuentaService.activarCuenta(numeroCuenta)).thenReturn(cuentaResponseDto);

    mockMvc
        .perform(patch(createEndpoint(numeroCuenta)))
        .andExpect(status().isOk())
        .andExpect(content().string(cuentaMovimientosResponseDtoMapped));
  }

  @Test
  public void testObtenerTransaccionesEnCuentaDoesntExistsException() throws Exception {
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
  public void testObtenerTransaccionesEnCuentaImpossibleException() throws Exception {
    doThrow(new ImpossibleException(""))
        .when(cuentaService)
        .buscarTransaccionesDeCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta) + "/transacciones"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testObtenerTransaccionesEnCuentaInactivaException() throws Exception {
    doThrow(new CuentaInactivaException(""))
        .when(cuentaService)
        .buscarTransaccionesDeCuentaPorId(numeroCuenta);

    mockMvc
        .perform(get(createEndpoint(numeroCuenta) + "/transacciones"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400114)));
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

  private CuentaRequestDto createCuentaRequestDto() {
    CuentaRequestDto cuentaDto = new CuentaRequestDto();
    cuentaDto.setBalance(500000);
    cuentaDto.setMoneda("P");
    cuentaDto.setTipoCuenta("A");
    cuentaDto.setTitular(12345678);
    return cuentaDto;
  }

  private CuentaResponseDto createCuentaResponseDto() {
    CuentaResponseDto cuentaDto = new CuentaResponseDto();
    cuentaDto.setBalance(500000);
    cuentaDto.setMoneda("P");
    cuentaDto.setTipoCuenta("A");
    cuentaDto.setTitular(12345678);
    cuentaDto.setActivo(true);
    return cuentaDto;
  }
}
