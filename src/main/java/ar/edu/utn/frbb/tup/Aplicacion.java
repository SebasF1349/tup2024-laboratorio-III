package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.presentation.input.MenuInputProcessor;

class Aplicacion {

  public static void main(String args[]) {
    MenuInputProcessor menuInputProcessor = new MenuInputProcessor();
    menuInputProcessor.renderMenu();
  }
}
