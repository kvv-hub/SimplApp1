package mypack;

import javax.naming.InsufficientResourcesException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Transfer implements Callable<Boolean> {
    private Account from;
    private Account to;
    private int amount;
    private static int idOps;
    private int id;
    private CountDownLatch countDownLatch;


    Transfer(Account from, Account to, int amount, CountDownLatch countDownLatch) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.id = ++idOps;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public Boolean call() throws  InsufficientResourcesException, InterruptedException  {
//        countDownLatch.countDown();
//        System.out.println(countDownLatch.getCount());
//        countDownLatch.await();
//        System.out.println("Оппа!!!");
        if (from.getLock().tryLock(2, TimeUnit.SECONDS))
            try {
                if (from.getBalans()<amount) {
//                    System.out.println(this.id + ": НЕДОСТАТОЧНО СРЕДСТВ " + from.getBalans() + " < " + amount);
                    throw new InsufficientResourcesException(""+this.id);
//                    return true;
                }
//                System.out.println("Loked from");
                if (to.getLock().tryLock(2, TimeUnit.SECONDS)) {
                    try {
//                        System.out.println("Loked to");
                        int t = (int) (Math.random()* 3000);
                        Thread.sleep(t);
                        from.withdrow(amount);
                        to.deposit(amount);
                        System.out.println(this.id+": Перевод выполнен! " + amount+" За - "+t);
                    } finally {
                        to.getLock().unlock();
                    }
                }
                else {
                    System.out.println("Счет пополнения блокирован!!! "+to.incFailedTransferCount());
                    return false;
                }
            } finally { from.getLock().unlock();
            }
        else {
            System.out.println("Счет списания блокирован!!! " + from.incFailedTransferCount());
            return false;
        }

        return true;

    }

}
