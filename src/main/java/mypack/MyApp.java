package mypack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@EnableAspectJAutoProxy
public class MyApp {

    public MyApp(){}

    public static void main(String[] args) {


        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("mypack");
        ctx.refresh();



        App app = ctx.getBean(App.class);
        MyAspect myAspect = ctx.getBean(MyAspect.class);

        app.logEvent();
        System.out.println(myAspect.getStrAspect());

        app.testMethod();




    }

}
