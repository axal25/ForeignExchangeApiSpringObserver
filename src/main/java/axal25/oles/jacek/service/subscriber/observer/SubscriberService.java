package axal25.oles.jacek.service.subscriber.observer;

import axal25.oles.jacek.model.dto.client.ExchangeRateDto;
import axal25.oles.jacek.service.event.NewCurrencyRatesEvent;
import axal25.oles.jacek.util.CsvUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static axal25.oles.jacek.constant.Constants.Commission.ASK;
import static axal25.oles.jacek.constant.Constants.Commission.BID;

@Service
public class SubscriberService implements ISubscriberService, ISubscriber<String> {
    private final Map<String, ExchangeRateDto> currencyToExchangeRate = new HashMap<>();

    @Override
    public Optional<ExchangeRateDto> getLatestCurrencyRateAsDto(final String currencyDashCurrency) {
        final String currencySlashCurrency = currencyDashCurrency.replaceAll("-", "/");
        return Optional.ofNullable(currencyToExchangeRate.get(currencySlashCurrency));
    }

    @Override
    public Optional<String> getLatestCurrencyRateAsCsv(final String currency) {
        return getLatestCurrencyRateAsDto(currency)
                .map(exchangeRateDto -> CsvUtils.toCsv(List.of(exchangeRateDto), false));
    }

    @EventListener
    public void listenOn(final NewCurrencyRatesEvent newCurrencyRatesEvent) {
        onMessage(newCurrencyRatesEvent.getNewCurrencyRates());
    }

    @Override
    public void onMessage(final String message) {
        final List<ExchangeRateDto> exchangeRateDtos = CsvUtils.parse(message);
        exchangeRateDtos.forEach(this::saveOrDiscardNewCurrencyRate);
    }

    private void saveOrDiscardNewCurrencyRate(final ExchangeRateDto parsed) {
        final ExchangeRateDto existing = currencyToExchangeRate.get(parsed.getInstrumentName());
        if (existing == null || existing.getTimestamp().isBefore(parsed.getTimestamp())) {
            final ExchangeRateDto withCommission = parsed.toBuilder()
                    .bid(parsed.getBid().add(BID.multiply(parsed.getBid())))
                    .ask(parsed.getAsk().add(ASK.multiply(parsed.getAsk())))
                    .build();
            currencyToExchangeRate.put(
                    parsed.getInstrumentName(),
                    withCommission);
        }
    }
}
