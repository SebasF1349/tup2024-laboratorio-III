package ar.edu.utn.frbb.tup.controller;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frbb.tup.controller.validator.MovimientoControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.BanelcoErrorException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.ImpossibleException;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasException;
import ar.edu.utn.frbb.tup.model.exception.MontoInsuficienteException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
@WebMvcTest(MovimientoController.class)
public class MovimientoControllerTest {

  @MockBean private MovimientoService movimientoService;
  @MockBean private MovimientoControllerValidator movimientoControllerValidator;
  private final String transferEndpoint = "/api/transfer";
  private final String depositEndpoint = "/api/deposit";
  private final String withdrawalEndpoint = "/api/withdrawal";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @InjectMocks private MovimientoController movimientoController;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testRealizarTransferenciaMissingInputData() throws Exception {
    TransferenciaRequestDto transferenciaDto = new TransferenciaRequestDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400104)))
        .andExpect(jsonPath("$.errors", aMapWithSize(4)))
        .andExpect(jsonPath("$.errors.monto", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.cuentaDestino", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.cuentaOrigen", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.moneda", is("must not be empty")));
  }

  @Test
  public void testRealizarTransferenciaWrongInputDataException() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new WrongInputDataException(""))
        .when(movimientoControllerValidator)
        .validate(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testRealizarTransferenciaCuentaDoesntExistsException() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new CuentaNoExistsException(""))
        .when(movimientoService)
        .realizarTransferencia(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testRealizarTransferenciaMontoInsuficienteException() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new MontoInsuficienteException(""))
        .when(movimientoService)
        .realizarTransferencia(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400131)));
  }

  @Test
  public void testRealizarTransferenciaMonedasDistintasException() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new MonedasDistintasException(""))
        .when(movimientoService)
        .realizarTransferencia(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400132)));
  }

  @Test
  public void testRealizarTransferenciaCorruptedDataInDbException() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new CorruptedDataInDbException(""))
        .when(movimientoService)
        .realizarTransferencia(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testRealizarTransferenciaBanelcoError() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new BanelcoErrorException("", 111))
        .when(movimientoService)
        .realizarTransferencia(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isServiceUnavailable())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(any(Integer.class))));
  }

  @Test
  public void testRealizarTransferenciaImpossibleException() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    String transferenciaDtoMapped = objectMapper.writeValueAsString(transferenciaDto);

    doThrow(new ImpossibleException())
        .when(movimientoService)
        .realizarTransferencia(transferenciaDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testRealizarTransferenciaSuccess() throws Exception {
    TransferenciaRequestDto transferenciaDto = createTransferenciaDto();
    TransferenciaResponseDto transferenciaResponseDto = createTransferenciaResponseDto();
    String transferenciaResponseDtoMapped =
        objectMapper.writeValueAsString(transferenciaResponseDto);

    when(movimientoService.realizarTransferencia(transferenciaDto))
        .thenReturn(transferenciaResponseDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(transferEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(transferenciaResponseDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(content().string(transferenciaResponseDtoMapped));
  }

  @Test
  public void testRealizarDepositoMissingInputData() throws Exception {
    DepositoRequestDto depositoDto = new DepositoRequestDto();
    String depositoDtoMapped = objectMapper.writeValueAsString(depositoDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400104)))
        .andExpect(jsonPath("$.errors", aMapWithSize(3)))
        .andExpect(jsonPath("$.errors.monto", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.cuenta", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.moneda", is("must not be empty")));
  }

  @Test
  public void testRealizarDepositoWrongInputDataException() throws Exception {
    DepositoRequestDto depositoDto = createDepositoDto();
    String depositoDtoMapped = objectMapper.writeValueAsString(depositoDto);

    doThrow(new WrongInputDataException(""))
        .when(movimientoControllerValidator)
        .validate(depositoDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testRealizarDepositoMonedasDistintasException() throws Exception {
    DepositoRequestDto depositoDto = createDepositoDto();
    String depositoDtoMapped = objectMapper.writeValueAsString(depositoDto);

    doThrow(new MonedasDistintasException(""))
        .when(movimientoService)
        .realizarDeposito(depositoDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400132)));
  }

  @Test
  public void testRealizarDepositoCuentaDoesntExistsException() throws Exception {
    DepositoRequestDto depositoDto = createDepositoDto();
    String depositoDtoMapped = objectMapper.writeValueAsString(depositoDto);

    doThrow(new CuentaNoExistsException("")).when(movimientoService).realizarDeposito(depositoDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testRealizarDepositoCorruptedDataInDbException() throws Exception {
    DepositoRequestDto depositoDto = createDepositoDto();
    String depositoDtoMapped = objectMapper.writeValueAsString(depositoDto);

    doThrow(new CorruptedDataInDbException(""))
        .when(movimientoService)
        .realizarDeposito(depositoDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testRealizarDepositoImpossibleException() throws Exception {
    DepositoRequestDto depositoDto = createDepositoDto();
    String depositoDtoMapped = objectMapper.writeValueAsString(depositoDto);

    doThrow(new ImpossibleException()).when(movimientoService).realizarDeposito(depositoDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testRealizarDepositoSuccess() throws Exception {
    DepositoRequestDto depositoDto = createDepositoDto();
    DepositoResponseDto depositoResponseDto = createDepositoResponseDto();
    String depositoResponseDtoMapped = objectMapper.writeValueAsString(depositoResponseDto);

    when(movimientoService.realizarDeposito(depositoDto)).thenReturn(depositoResponseDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(depositEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(depositoResponseDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(content().string(depositoResponseDtoMapped));
  }

  @Test
  public void testRealizarRetiroMissingInputData() throws Exception {
    RetiroRequestDto retiroDto = new RetiroRequestDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400104)))
        .andExpect(jsonPath("$.errors", aMapWithSize(3)))
        .andExpect(jsonPath("$.errors.monto", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.cuenta", is("must be greater than 0")))
        .andExpect(jsonPath("$.errors.moneda", is("must not be empty")));
  }

  @Test
  public void testRealizarRetiroWrongInputDataException() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    doThrow(new WrongInputDataException(""))
        .when(movimientoControllerValidator)
        .validate(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testRealizarRetiroMonedasDistintasException() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    doThrow(new MonedasDistintasException("")).when(movimientoService).realizarRetiro(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400132)));
  }

  @Test
  public void testRealizarRetiroCuentaDoesntExistsException() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    doThrow(new CuentaNoExistsException("")).when(movimientoService).realizarRetiro(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404102)));
  }

  @Test
  public void testRealizarRetiroCorruptedDataInDbException() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    doThrow(new CorruptedDataInDbException("")).when(movimientoService).realizarRetiro(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testRealizarRetiroImpossibleException() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    doThrow(new ImpossibleException()).when(movimientoService).realizarRetiro(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500100)));
  }

  @Test
  public void testRealizarRetiroMontoInsuficienteException() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    String retiroDtoMapped = objectMapper.writeValueAsString(retiroDto);

    doThrow(new MontoInsuficienteException("")).when(movimientoService).realizarRetiro(retiroDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroDtoMapped);
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400131)));
  }

  @Test
  public void testRealizarRetiroSuccess() throws Exception {
    RetiroRequestDto retiroDto = createRetiroDto();
    RetiroResponseDto retiroResponseDto = createRetiroResponseDto();
    String retiroResponseDtoMapped = objectMapper.writeValueAsString(retiroResponseDto);

    when(movimientoService.realizarRetiro(retiroDto)).thenReturn(retiroResponseDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(withdrawalEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(retiroResponseDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(content().string(retiroResponseDtoMapped));
  }

  private TransferenciaRequestDto createTransferenciaDto() {
    TransferenciaRequestDto transferenciaDto = new TransferenciaRequestDto();
    transferenciaDto.setCuentaOrigen(1);
    transferenciaDto.setCuentaDestino(2);
    transferenciaDto.setMonto(1000);
    transferenciaDto.setMoneda("D");
    return transferenciaDto;
  }

  private TransferenciaResponseDto createTransferenciaResponseDto() {
    TransferenciaResponseDto transferenciaResponseDto = new TransferenciaResponseDto();
    transferenciaResponseDto.setCuentaOrigen(1);
    transferenciaResponseDto.setCuentaDestino(2);
    transferenciaResponseDto.setMonto(1000);
    transferenciaResponseDto.setMontoDebitado(1000);
    transferenciaResponseDto.setMoneda("D");
    return transferenciaResponseDto;
  }

  private DepositoRequestDto createDepositoDto() {
    DepositoRequestDto depositoDto = new DepositoRequestDto();
    depositoDto.setCuenta(1);
    depositoDto.setMonto(1000);
    depositoDto.setMoneda("D");
    return depositoDto;
  }

  private DepositoResponseDto createDepositoResponseDto() {
    DepositoResponseDto depositoResponseDto = new DepositoResponseDto();
    depositoResponseDto.setCuenta(1);
    depositoResponseDto.setMonto(1000);
    depositoResponseDto.setMoneda("D");
    return depositoResponseDto;
  }

  private RetiroRequestDto createRetiroDto() {
    RetiroRequestDto retiroDto = new RetiroRequestDto();
    retiroDto.setCuenta(1);
    retiroDto.setMonto(1000);
    retiroDto.setMoneda("D");
    return retiroDto;
  }

  private RetiroResponseDto createRetiroResponseDto() {
    RetiroResponseDto retiroResponseDto = new RetiroResponseDto();
    retiroResponseDto.setCuenta(1);
    retiroResponseDto.setMonto(1000);
    retiroResponseDto.setMoneda("D");
    return retiroResponseDto;
  }
}
