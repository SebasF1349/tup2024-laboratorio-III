package ar.edu.utn.frbb.tup.view;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Direccion;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;

public class ClienteModifyProcessor extends BaseInputProcessor {
  public Cliente modifyCliente(Cliente cliente) {

    String enBlanco = " (o deje en blanca para mantener el valor actual):";
    clearScreen();

    System.out.println("Nombre actual: " + cliente.getNombre());
    String nombre = this.getStringInput("Ingrese el nuevo nombre del cliente" + enBlanco);
    if (nombre.trim() != "") {
      cliente.setNombre(nombre);
    }

    System.out.println("Apellido actual: " + cliente.getApellido());
    String apellido = this.getStringInput("Ingrese el nuevo apellido del cliente" + enBlanco);
    if (apellido.trim() != "") {
      cliente.setApellido(apellido);
    }

    System.out.println("Dirección actual: " + cliente.getDireccion());
    String modificarDireccion =
        this.getStringInput("¿Desea modificar la dirección? [S] para editar:");
    if (modificarDireccion.equalsIgnoreCase("y")) {
      System.out.println("Calle actual: " + cliente.getDireccion().getCalle());
      String calle = this.getStringInput("Ingrese la calle de la dirección del cliente");
      System.out.println("numero actual: " + cliente.getDireccion().getNumero());
      String numero = this.getStringInput("Ingrese el numero de la dirección del cliente");
      System.out.println("departamento actual: " + cliente.getDireccion().getDepartamento());
      String departamento =
          this.getStringInput("Ingrese la departamento de la dirección del cliente");
      System.out.println("ciudad actual: " + cliente.getDireccion().getCiudad());
      String ciudad = this.getStringInput("Ingrese la ciudad de la dirección del cliente");
      Direccion direccionCliente = new Direccion(calle, numero, departamento, ciudad);
      cliente.setDireccion(direccionCliente);
    }

    System.out.println("Teléfono actual: " + cliente.getNroTelefono());
    String nroTelefono = this.getStringInput("Ingrese el teléfono del cliente" + enBlanco);
    if (nroTelefono.trim() != "") {
      cliente.setNroTelefono(nroTelefono);
    }

    System.out.println("Fecha de nacimiento actual: " + cliente.getFechaNacimiento());
    LocalDate fechaNacimiento = this.getDateInput("Ingrese la fecha de nacimiento del cliente");
    cliente.setFechaNacimiento(fechaNacimiento);

    System.out.println("Tipo de persona actual: " + cliente.getTipoPersona());
    System.out.println("Ingrese el tipo de persona Física[F] o Jurídica[J]");
    String tipoPersonaStr = scanner.nextLine().toUpperCase();
    while (!tipoPersonaStr.equalsIgnoreCase("F") && !tipoPersonaStr.equalsIgnoreCase("J")) {
      System.out.println("Tipo de persona inválido. Ingrese F (FÍSICA) o J (JURIDICA)");
      tipoPersonaStr = scanner.nextLine().toUpperCase();
    }
    TipoPersona tipoPersona = TipoPersona.fromString(tipoPersonaStr);
    cliente.setTipoPersona(tipoPersona);

    System.out.println("Fecha de alta actual: " + cliente.getFechaAlta());
    LocalDate fechaAlta = this.getDateInput("Ingrese la fecha de alta del cliente:");
    cliente.setFechaAlta(fechaAlta);

    System.out.println(cliente);
    String confirmacion = getStringInput("¿Los datos del cliente son correctos? [N] para editar:");

    if (confirmacion.equalsIgnoreCase("n")) {
      cliente = this.modifyCliente(cliente);
    }

    clearScreen();
    return cliente;
  }
}
