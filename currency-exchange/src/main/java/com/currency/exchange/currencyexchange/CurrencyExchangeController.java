package com.currency.exchange.currencyexchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    @Autowired
    private Environment environment;

    @Autowired
    private CurrencyExchangeRepository repository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ResponseEntity<CurrencyExchange> getExchange(@PathVariable("from") String from, @PathVariable("to") String to) throws Exception {
        try {
            String port = environment.getProperty("local.server.port");

            CurrencyExchange currencyExchangeData = repository.findByFromAndTo(from, to);

            if (currencyExchangeData == null) {
                throw new CustomExceptions.EntityDoesntExistException("Data not found!");
            }

            CurrencyExchange exchange = new CurrencyExchange(currencyExchangeData.getId(), from, to, currencyExchangeData.getConversionMultiple(), port);
            return ResponseEntity.ok().body(exchange);
        } catch (CustomExceptions.EntityDoesntExistException e){
            throw e;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

	/*public String fallbackMethod(Exception ex) {
		return "Circuit breaker fallback method triggered! You can't send more than 2 requests within 60 seconds to this microservice";
	}*/
}
