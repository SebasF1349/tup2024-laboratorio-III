package ar.edu.utn.frbb.tup.controller;

import static org.hamcrest.Matchers.*;
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

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @InjectMocks private MovimientoController movimientoController;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testRealizarTransferenciaMissingInputData() throws Exception {
    TransferenciaDto transferenciaDto = new TransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
  public void testRealizarTransferenciaCuentaDoesntExistsFail() throws Exception {
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
  public void testRealizarTransferenciaSuccess() throws Exception {
    TransferenciaDto transferenciaDto = createTransferenciaDto();
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
        .andExpect(status().isCreated())
        .andExpect(content().string(transferenciaResponseDtoMapped));
  }

  private TransferenciaDto createTransferenciaDto() {
    TransferenciaDto transferenciaDto = new TransferenciaDto();
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
}
