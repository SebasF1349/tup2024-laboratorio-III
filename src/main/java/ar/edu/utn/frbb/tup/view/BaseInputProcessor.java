package ar.edu.utn.frbb.tup.view;

import java.time.LocalDate;
import java.util.Scanner;

public class BaseInputProcessor {

  protected Scanner scanner = new Scanner(System.in);

  protected static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public String getStringInput(String question) {
    System.out.println(question);
    return scanner.nextLine();
  }

  public LocalDate getDateInput(String question) {
    System.out.println(question + " (Formato: YYYY-MM-DD):");
    LocalDate fecha = null;
    boolean fechaValida = false;
    while (!fechaValida) {
      try {
        fecha = LocalDate.parse(scanner.nextLine());
        fechaValida = true;
      } catch (Exception e) {
        System.out.println("Formato de fecha inválido. Ingrese la fecha en formato YYYY-MM-DD:");
      }
    }
    return fecha;
  }
}
