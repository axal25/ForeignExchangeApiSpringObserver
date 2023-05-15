package axal25.oles.jacek.controller;

import axal25.oles.jacek.dependency.external.ExternalRequestContractDto;
import axal25.oles.jacek.dependency.external.ExternalResponseContractDto;
import axal25.oles.jacek.service.publisher.observable.subject.IPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static axal25.oles.jacek.constant.Constants.Paths.*;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping(PUBLISHER_CONTROLLER)
public class PublisherController {

    @Autowired
    private IPublisherService publisherService;

    @PostMapping(value = PUBLISHER_POST_APPLICATION_JSON)
    public ResponseEntity<ExternalResponseContractDto> receiveNewCurrencyRatesApplicationJson(
            @RequestBody ExternalRequestContractDto externalRequestContractDto) {
        String publishExceptionMessage = publisherService.publishNewCurrencyRates(externalRequestContractDto.getContent());
        if (publishExceptionMessage != null) {
            return new ResponseEntity<>(
                    ExternalResponseContractDto.builder()
                            .content(String.format("Unpublished. Cause: %s", publishExceptionMessage))
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ExternalResponseContractDto.builder()
                        .content("Published.")
                        .build(),
                HttpStatus.CREATED);
    }

    @PostMapping(value = PUBLISHER_POST_TEXT_PLAIN, consumes = TEXT_PLAIN_VALUE, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> receiveNewCurrencyRatesTextPlain(
            @RequestBody String newCurrencyRates) {
        String publishExceptionMessage = publisherService.publishNewCurrencyRates(newCurrencyRates);
        if (publishExceptionMessage != null) {
            return new ResponseEntity<>(
                    String.format("Unpublished. Cause: %s", publishExceptionMessage),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                "Published.",
                HttpStatus.CREATED);
    }
}
