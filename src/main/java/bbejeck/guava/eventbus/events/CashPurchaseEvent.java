package bbejeck.guava.eventbus.events;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:12 PM
 */

public class CashPurchaseEvent extends PurchaseEvent {

    private String item;

    public CashPurchaseEvent(long amount, String item) {
        super(amount);
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "CashPurchaseEvent{" +
                "item='" + item + '\'' +
                "} " + super.toString();
    }
}
