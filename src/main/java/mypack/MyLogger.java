package mypack;

import java.util.concurrent.Callable;

public class MyLogger implements Runnable {
    private Account a;
    private  Account b;

    MyLogger(Account a, Account b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        try {
            System.out.println("====  Блокировки откуда " + this.a.getFailCounter());
            System.out.println("===== Блокировки куда " + this.b.getFailCounter());
        } catch (Exception e) {
            System.out.println("Ошиба!");
        }

    }


}
