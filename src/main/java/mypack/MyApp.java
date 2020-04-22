package mypack;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
@EnableAspectJAutoProxy
public class MyApp {

    public MyApp(){}

    public static void main(String[] args) throws InterruptedException {


        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("mypack");
        ctx.refresh();

        Oprations op1 = ctx.getBean(Oprations.class);
        Oprations op2 = ctx.getBean(Oprations.class);
        Account a = ctx.getBean(Account.class);
        Account b = ctx.getBean(Account.class);

        System.out.println("Баланс a = "+a.getBalans()+"\n\rБаланс a = "+b.getBalans());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    op1.transfer(a,b,333);
                    System.out.println("Баланс a = "+a.getBalans()+"\n\rБаланс a = "+b.getBalans());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }).start();



        op2.transfer(b,a,555);
        System.out.println("Баланс a = "+a.getBalans()+"\n\rБаланс a = "+b.getBalans());


//        App app = ctx.getBean(App.class);
//        MyAspect myAspect = ctx.getBean(MyAspect.class);
//
//        app.logEvent();
//        System.out.println(myAspect.getStrAspect());
//
//        app.testMethod();

    }

}
