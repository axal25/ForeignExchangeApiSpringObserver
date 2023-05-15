package axal25.oles.jacek.controller;

import axal25.oles.jacek.model.dto.client.ExchangeRateDto;
import axal25.oles.jacek.service.subscriber.observer.ISubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static axal25.oles.jacek.constant.Constants.MediaType.TEXT_CSV_VALUE;
import static axal25.oles.jacek.constant.Constants.Paths.*;

@RestController
@RequestMapping(SUBSCRIBER_CONTROLLER)
public class SubscriberController {

    @Autowired
    private ISubscriberService subscriberService;

    @GetMapping(value = SUBSCRIBER_GET_APPLICATION_JSON + "/{currency}")
    public ResponseEntity<ExchangeRateDto> getLatestCurrencyRateJson(@PathVariable("currency") String currency) {
        Optional<ExchangeRateDto> optionalExchangeRateDto = subscriberService.getLatestCurrencyRateAsDto(currency);
        return new ResponseEntity<>(
                optionalExchangeRateDto.orElse(null),
                optionalExchangeRateDto.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = SUBSCRIBER_GET_TEXT_CSV + "/{currency}", produces = {TEXT_CSV_VALUE})
    public ResponseEntity<String> getLatestCurrencyRateCsv(
            @PathVariable("currency") String currency) {
        Optional<String> optionalExchangeRateCsv = subscriberService.getLatestCurrencyRateAsCsv(currency);
        return new ResponseEntity<>(
                optionalExchangeRateCsv.orElse(null),
                optionalExchangeRateCsv.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }
}
