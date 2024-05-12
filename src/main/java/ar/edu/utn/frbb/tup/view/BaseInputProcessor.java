package ar.edu.utn.frbb.tup.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BaseInputProcessor {

  protected Scanner scanner = new Scanner(System.in);

  protected static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public int getMultipleOptionsInput(String question, List<String> menuOptions) {
    System.out.println(question);
    for (int i = 0; i < menuOptions.size(); i++) {
      System.out.println((i + 1) + ". " + menuOptions.get(i));
    }
    System.out.println("Ingrese su opción (1-" + menuOptions.size() + "): ");
    boolean optValida = false;
    int opt = 0;
    while (!optValida) {
      try {
        String optValue = scanner.nextLine();
        opt = Integer.parseInt(optValue);
        if (opt > 0 && opt <= menuOptions.size()) {
          optValida = true;
        } else {
          System.out.println(question);
          optValue = scanner.nextLine();
        }
      } catch (Exception e) {
        System.out.println("Opción inválida. Ingrese su opción (1-" + menuOptions.size() + "): ");
      }
    }
    return opt;
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

  public String getEnumInput(String question, String opt1, String opt2) {
    System.out.println(question);
    String enumValue = scanner.nextLine();
    while (!enumValue.equalsIgnoreCase(opt1) && !enumValue.equalsIgnoreCase(opt2)) {
      System.out.println("Valor inválido");
      System.out.println(question);
      enumValue = scanner.nextLine();
    }
    return enumValue;
  }

  public boolean getBooleanInput(String question) {
    String yesOrNo = " Ingrese Si[S] o No[N].";
    System.out.println(question + yesOrNo);
    String answer = scanner.nextLine().toUpperCase();
    while (!answer.equals("S")
        && !answer.equals("N")
        && !answer.equals("SI")
        && !answer.equals("NO")) {
      System.out.println("Valor inválido");
      System.out.println(question + yesOrNo);
      answer = scanner.nextLine().toUpperCase();
    }
    return answer.equals("S") || answer.equals("SI");
  }
}
