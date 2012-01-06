package bbejeck.guava.eventbus.events;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 12:50 PM
 */

public class CreditPurchaseEvent extends PurchaseEvent {

    private String creditCardNumber;
    private String item;

    public CreditPurchaseEvent(long amount, String item, String creditCardNumber) {
        super(amount);
        this.item = item;
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "CreditPurchaseEvent{" +
                "creditCardNumber='" + creditCardNumber + '\'' +
                ", item='" + item + '\'' +
                "} " + super.toString();
    }
}
