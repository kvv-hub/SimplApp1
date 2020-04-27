package mypack;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;


@Service
@EnableAspectJAutoProxy
@EnableAsync
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

//        new Thread(() ->  {
//                try {
//                    op1.transfer(a,b,333);
//                    System.out.println("Баланс a = "+a.getBalans()+"\n\rБаланс a = "+b.getBalans());
//                } catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//        }).start();


//        op2.transfer(b,a,555);
//        System.out.println("Баланс a = "+a.getBalans()+"\n\rБаланс a = "+b.getBalans());

        CountDownLatch countDownLatch = new CountDownLatch(10);

        ExecutorService service = Executors.newFixedThreadPool(10);

        ScheduledExecutorService shedservice = Executors.newScheduledThreadPool(2);
         shedservice.scheduleAtFixedRate(new MyLogger(a,b),1,1, TimeUnit.SECONDS);

        List<Callable<Boolean>> tasks = new ArrayList<>();


        for (int i=0; i<10; ++i){
            tasks.add(new Transfer(a,b,(int) Math.round(Math.random()*400), countDownLatch));
        }

        List<Future<Boolean>> futures;
        boolean[] nomany = new boolean[10];

        int cnt=10;
        do {
            System.out.println("Перезапуск "+cnt);
            futures = service.invokeAll(tasks);

            for (int i=0; i<cnt; ++i) {
                try {
                    if (futures.get(i).get()) {
                        countDownLatch.countDown();
                        tasks.remove(i);
                        --cnt;
                    }
                } catch (ExecutionException e) {
//                    System.out.println("ОЙ ОЙ ОЙ ====== "+e.getMessage());
                    countDownLatch.countDown();
                    tasks.remove(i);
                    --cnt;
                    int r = Integer.parseInt(e.getMessage().substring(e.getMessage().length()-1));
                    if (r >= 0 & r<10) {
                        nomany[r] = true;
                    }
                } catch (InterruptedException e) {
                    System.out.println("УУУУУУУ  ====== "+e.getMessage());
                }
            }
        } while (countDownLatch.getCount()>0);


        countDownLatch.await();
        for (Future<Boolean> fut: futures){
            try {
                System.out.println(fut.get());
            } catch (ExecutionException e){
//                System.out.println("Ой2 ОЙ2 ОЙ2 ====== "+e.getMessage());
            }
        }
        for (int i=0; i<10; ++i) {
            if(nomany[i]) {
                System.out.println("Недостаточно средств на счете для операции "+i);
            }
        }


        service.shutdown();

        System.out.println("shutdown");

        shedservice.shutdown();

        service.awaitTermination(1, TimeUnit.MILLISECONDS);


    }

}
