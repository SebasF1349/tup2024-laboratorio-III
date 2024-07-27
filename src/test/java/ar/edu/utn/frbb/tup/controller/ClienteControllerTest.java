package ar.edu.utn.frbb.tup.controller;

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

import ar.edu.utn.frbb.tup.controller.validator.ClienteControllerValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExistsException;
import ar.edu.utn.frbb.tup.model.exception.CorruptedDataInDbException;
import ar.edu.utn.frbb.tup.model.exception.WrongInputDataException;
import ar.edu.utn.frbb.tup.service.ClienteService;
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
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

  @MockBean private ClienteService clienteService;
  @MockBean private ClienteControllerValidator clienteControllerValidator;
  private final long dniCliente = 12345678;
  private final String endpoint = "/api/cliente";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @InjectMocks private ClienteController clienteController;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testObtenerClienteDoesntExistsFail() throws Exception {
    doThrow(new ClienteNoExistsException("")).when(clienteService).buscarClientePorDni(dniCliente);

    mockMvc
        .perform(get(createEndpoint(dniCliente)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404101)));
  }

  @Test
  public void testObtenerClienteSuccess() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    Mockito.when(clienteService.buscarClientePorDni(dniCliente)).thenReturn(clienteDto);

    mockMvc
        .perform(get(createEndpoint(dniCliente)))
        .andExpect(status().isOk())
        .andExpect(content().string(clienteDtoMapped));
  }

  @Test
  public void testCrearClienteIncorrectDataFail() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    doThrow(new WrongInputDataException("")).when(clienteControllerValidator).validate(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testCrearClienteAlreadyExistsFail() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    doThrow(new ClienteAlreadyExistsException(""))
        .when(clienteService)
        .darDeAltaCliente(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400110)));
  }

  @Test
  public void testCrearClienteMenorDeEdadFail() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    doThrow(new ClienteMenorDeEdadException("")).when(clienteService).darDeAltaCliente(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400111)));
  }

  @Test
  public void testCrearClienteSuccess() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isCreated())
        .andExpect(content().string(clienteDtoMapped));
  }

  @Test
  public void testEliminarClienteCorruptedDataInDBException() throws Exception {
    doThrow(new CorruptedDataInDbException("")).when(clienteService).eliminarCliente(dniCliente);

    mockMvc
        .perform(delete(createEndpoint(dniCliente)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(500101)));
  }

  @Test
  public void testEliminarClienteDoesntExistsFail() throws Exception {
    doThrow(new ClienteNoExistsException("")).when(clienteService).eliminarCliente(dniCliente);

    mockMvc
        .perform(delete(createEndpoint(dniCliente)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404101)));
  }

  @Test
  public void testEliminarClienteSuccess() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    Mockito.when(clienteService.eliminarCliente(dniCliente)).thenReturn(clienteDto);

    mockMvc
        .perform(delete(createEndpoint(dniCliente)))
        .andExpect(status().isOk())
        .andExpect(content().string(clienteDtoMapped));
  }

  @Test
  public void testActualizarClienteIncorrectDataFail() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    doThrow(new WrongInputDataException("")).when(clienteControllerValidator).validate(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400101)));
  }

  @Test
  public void testActualizarClienteNoExistsFail() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    doThrow(new ClienteNoExistsException("")).when(clienteService).actualizarCliente(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404101)));
  }

  @Test
  public void testActualizarClienteMenorDeEdadFail() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    doThrow(new ClienteMenorDeEdadException("")).when(clienteService).actualizarCliente(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(400111)));
  }

  @Test
  public void testActualizarClienteSuccess() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    when(clienteService.actualizarCliente(clienteDto)).thenReturn(clienteDto);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put(getEndpoint())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(clienteDtoMapped);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(content().string(clienteDtoMapped));
  }

  @Test
  public void testActivarClienteDoesntExistsFail() throws Exception {
    doThrow(new ClienteNoExistsException("")).when(clienteService).activarCliente(dniCliente);

    mockMvc
        .perform(patch(createEndpoint(dniCliente)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.errorCode", is(404101)));
  }

  @Test
  public void testActivarClienteSuccess() throws Exception {
    ClienteDto clienteDto = createClienteDto();
    String clienteDtoMapped = objectMapper.writeValueAsString(clienteDto);

    Mockito.when(clienteService.activarCliente(dniCliente)).thenReturn(clienteDto);

    mockMvc
        .perform(patch(createEndpoint(dniCliente)))
        .andExpect(status().isOk())
        .andExpect(content().string(clienteDtoMapped));
  }

  private String createEndpoint(long end) {
    return endpoint + "/" + end;
  }

  private String getEndpoint() {
    return endpoint;
  }

  private ClienteDto createClienteDto() {
    ClienteDto clienteDto = new ClienteDto();
    clienteDto.setDni(dniCliente);
    clienteDto.setNombre("Nombre");
    clienteDto.setApellido("Apellido");
    clienteDto.setFechaNacimiento("1990-01-01");
    clienteDto.setTipoPersona("F");
    clienteDto.setBanco("");
    return clienteDto;
  }

  // private Cliente createCliente() {
  //   ClienteDto clienteDto = createClienteDto();
  //   return new Cliente(clienteDto);
  // }
}
