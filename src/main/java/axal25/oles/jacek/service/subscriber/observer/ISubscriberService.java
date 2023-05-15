package axal25.oles.jacek.service.subscriber.observer;

import axal25.oles.jacek.model.dto.client.ExchangeRateDto;

import java.util.Optional;

public interface ISubscriberService {
    Optional<ExchangeRateDto> getLatestCurrencyRateAsDto(String currency);

    Optional<String> getLatestCurrencyRateAsCsv(String currency);
}
