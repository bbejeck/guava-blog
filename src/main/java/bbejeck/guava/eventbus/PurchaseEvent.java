package bbejeck.guava.eventbus;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:12 PM
 */

public class PurchaseEvent {

    String item;
    String amount;

    public PurchaseEvent() {
    }

    public PurchaseEvent(String amount, String item) {
        this.amount = amount;
        this.item = item;
    }

    @Override
    public String toString() {
        return "PurchaseEvent{" +
                "item='" + item + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
