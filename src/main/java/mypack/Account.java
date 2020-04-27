package mypack;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope("prototype")
public class Account {
    private int balans;
    private Lock lock;
    private AtomicInteger failCounter;

    public Account(int balans) {
        this.balans = balans;
        this.lock = new ReentrantLock();
        this.failCounter = new AtomicInteger();
        this.failCounter.set(0);
    }

    public Account() {
        this.balans = 1000;
        this.lock = new ReentrantLock();
        this.failCounter = new AtomicInteger();
        this.failCounter.set(0);
    }

    public AtomicInteger getFailCounter() {
        return failCounter;
    }

    public int incFailedTransferCount(){
        return(failCounter.incrementAndGet());
    }


    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public int getBalans() {
        return balans;
    }

    public void setBalans(int balans) {
        this.balans = balans;
    }

    public void withdrow(int amount){
        this.balans-=amount;
    }
    public void deposit(int amount){
        this.balans+=amount;
    }
}
