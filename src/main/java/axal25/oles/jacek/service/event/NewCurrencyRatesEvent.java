package axal25.oles.jacek.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewCurrencyRatesEvent extends ApplicationEvent {
    private final String newCurrencyRates;

    public NewCurrencyRatesEvent(Object source, String newCurrencyRates) {
        super(source);
        this.newCurrencyRates = newCurrencyRates;
    }
}
