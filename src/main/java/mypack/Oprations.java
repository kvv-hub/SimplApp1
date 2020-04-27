package mypack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class Oprations {

    private static int idOps;
    private int id;

    public Oprations() {
        this.id = ++idOps;
    }

    public Boolean transfer(Account from, Account to, int amount) throws InterruptedException{
        if (from.getBalans()<amount) {
            System.out.println(this.id+": Недостаточно средств на счете");
            return false;
        }
        if (from.getLock().tryLock(1, TimeUnit.SECONDS))
            try {
                System.out.println("Loked from");
                if (to.getLock().tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Loked to");
                        from.withdrow(amount);
                        to.deposit(amount);
                        System.out.println(this.id+": Перевод выполнен!");
                    } finally {
                        to.getLock().unlock();
                    }
                }
                else {
                    System.out.println("Счет пополнения блокирован!!! "+to.incFailedTransferCount());
                }
            } finally { from.getLock().unlock();
            }
            else {
            System.out.println("Счет списания блокирован!!! " + from.incFailedTransferCount());
        }

        return true;
    }

}
