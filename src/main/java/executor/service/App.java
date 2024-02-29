package executor.service;

import executor.service.utils.WebDriverInitializer;
import org.openqa.selenium.WebDriver;

import java.util.Set;

/**
 * Hello world!
 *
 */
//public class App
//{
//    public static void main( String[] args ) {
//        System.out.println( "Hello World!" );
//    }
//}


public class App
{
    public static void main( String[] args ) {


        new Thread(() -> {
            try {
                new App().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                new App().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                new App().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void start() {

        WebDriverInitializer webDriverInitializer = new WebDriverInitializer();
        WebDriver webDriver = webDriverInitializer.init();

        try {
            webDriver.get("https://ipinfo.io/?utm_term=ip%20tracking%20api&utm_campaign=ipdata&utm_source=adwords&utm_medium=ppc&hsa_acc=4130784448&hsa_cam=20002935736&hsa_grp=151217896794&hsa_ad=656257099378&hsa_src=g&hsa_tgt=kwd-898831099421&hsa_kw=ip%20tracking%20api&hsa_mt=p&hsa_net=adwords&hsa_ver=3&gad_source=1&gclid=EAIaIQobChMIpPHC1dXJhAMVnleRBR3KXQ5xEAAYAiAAEgJbE_D_BwE");
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webDriver.quit();
    }
}