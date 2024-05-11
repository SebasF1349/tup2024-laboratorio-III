package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Direccion;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;

public class ClienteInputProcessor extends BaseInputProcessor {

  public Cliente ingresarCliente() {

    Cliente cliente = new Cliente();

    clearScreen();

    String dni = this.getStringInput("Ingrese el dni del cliente:");
    cliente.setDni(dni);

    String nombre = this.getStringInput("Ingrese el nombre del cliente:");
    cliente.setNombre(nombre);

    String apellido = this.getStringInput("Ingrese el apellido del cliente:");
    cliente.setApellido(apellido);

    String calle = this.getStringInput("Ingrese la calle de la dirección del cliente:");
    String numero = this.getStringInput("Ingrese el numero de la dirección del cliente:");
    String departamento = this.getStringInput("Ingrese el número de departamento del cliente:");
    String ciudad = this.getStringInput("Ingrese la ciudad de residencia del cliente:");
    Direccion direccionCliente = new Direccion(calle, numero, departamento, ciudad);
    cliente.setDireccion(direccionCliente);

    String nroTelefono = this.getStringInput("Ingrese el teléfono del cliente:");
    cliente.setNroTelefono(nroTelefono);

    LocalDate fechaNacimiento = this.getDateInput("Ingrese la fecha de nacimiento del cliente:");
    cliente.setFechaNacimiento(fechaNacimiento);

    System.out.println("Ingrese el tipo de persona Física[F] o Jurídica[J]:");
    String tipoPersonaStr = scanner.nextLine().toUpperCase();
    while (!tipoPersonaStr.equalsIgnoreCase("F") && !tipoPersonaStr.equalsIgnoreCase("J")) {
      System.out.println("Tipo de persona inválido. Ingrese F (FÍSICA) o J (JURIDICA):");
      tipoPersonaStr = scanner.nextLine().toUpperCase();
    }
    TipoPersona tipoPersona = TipoPersona.fromString(tipoPersonaStr);
    cliente.setTipoPersona(tipoPersona);

    LocalDate fechaAlta = this.getDateInput("Ingrese la fecha de alta del cliente:");
    cliente.setFechaAlta(fechaAlta);

    System.out.println(cliente);
    String confirmacion =
        getStringInput("¿Los datos del nuevo cliente son correctos? [N] para editar:");

    if (confirmacion.equalsIgnoreCase("n")) {
      ClienteModifyProcessor clienteModifyProcessor = new ClienteModifyProcessor();
      cliente = clienteModifyProcessor.modifyCliente(cliente);
    }

    clearScreen();
    return cliente;
  }
}
