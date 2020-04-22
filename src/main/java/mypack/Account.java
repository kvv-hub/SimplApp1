package mypack;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope("prototype")
public class Account {
    private int balans;
    private Lock lock;

    public Account(int balans) {
        this.balans = balans;
        this.lock = new ReentrantLock();
    }

    public Account() {
        this.balans = 1000;
        this.lock = new ReentrantLock();
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
