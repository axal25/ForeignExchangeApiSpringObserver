package axal25.oles.jacek.service.publisher.observable.subject;

import axal25.oles.jacek.service.event.NewCurrencyRatesEvent;
import axal25.oles.jacek.util.ExceptionUtils;
import axal25.oles.jacek.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PublisherService implements IPublisherService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String publishNewCurrencyRates(String newCurrencyRates) {
        if (StringUtils.isNullOrEmptyOrBlank(newCurrencyRates)) {
            return "CSV content with currency rates cannot be null, empty, or blank.";
        }
        try {
            applicationEventPublisher.publishEvent(
                    new NewCurrencyRatesEvent(
                            this,
                            newCurrencyRates));
        } catch (Exception e) {
            return ExceptionUtils.getUsefulMessage(e);
        }
        return null;
    }
}
