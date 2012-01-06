package bbejeck.guava.eventbus.events;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 12:42 PM
 */

public abstract class PurchaseEvent {
    protected long amount;

    public PurchaseEvent(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PurchaseEvent{" +
                "amount=" + amount +
                '}';
    }
}
