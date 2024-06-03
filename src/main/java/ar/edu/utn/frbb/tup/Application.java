package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.presentation.input.MenuInputProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

  public static void main(String args[]) {
    try (ConfigurableApplicationContext applicationContext =
        new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
      MenuInputProcessor menuInputProcessor = applicationContext.getBean(MenuInputProcessor.class);
      menuInputProcessor.renderMenu();
    } catch (BeansException e) {
      e.printStackTrace();
    }

    // ConfigurableApplicationContext applicationContext =
    //    new AnnotationConfigApplicationContext(ApplicationConfig.class);
    //
    // MenuInputProcessor menuInputProcessor = applicationContext.getBean(MenuInputProcessor.class);
    // menuInputProcessor.renderMenu();
  }
}
