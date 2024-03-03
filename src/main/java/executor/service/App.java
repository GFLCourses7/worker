package executor.service;

import executor.service.factory.AbstractFactory;
import executor.service.factory.DIFactory;
import executor.service.utils.WebDriverInitializer;
import org.openqa.selenium.WebDriver;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        AbstractFactory abstractFactory = new DIFactory();
        WebDriverInitializer webDriverInitializer = abstractFactory.create(WebDriverInitializer.class);
        WebDriver webDriver = webDriverInitializer.init();
        webDriver.get("https://google.com");
        webDriver.quit();
        System.out.println( "Hello World!" );
    }
}
