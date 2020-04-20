package mypack;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
public class App {


    public void App(){}

    public void logEvent() {
        System.out.println("logEvent OK");
    }

    public String testMethod() {
        System.out.println("test OK");
        return "Прошла халява";
    }

}
