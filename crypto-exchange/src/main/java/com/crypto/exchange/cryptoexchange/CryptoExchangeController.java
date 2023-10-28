package com.crypto.exchange.cryptoexchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoExchangeController {

    @Autowired
    private Environment environment;
    @Autowired
    private CryptoExchangeRepository repository;

    @GetMapping("/crypto-exchange/from/{from}/to/{to}")
    public ResponseEntity<CryptoExchange> getExchange(@PathVariable("from") String from, @PathVariable("to") String to) throws Exception{

        try{
            String port = environment.getProperty("local.server.port");

            CryptoExchange cryptoExchangeData = repository.findByFromAndTo(from, to);
            if (cryptoExchangeData == null) {
                throw new CustomExceptions.EntityDoesntExistException("Data not found!");
            }

            CryptoExchange exchange = new CryptoExchange(cryptoExchangeData.getId(), from, to, cryptoExchangeData.getConversionMultiple(), port);
            return ResponseEntity.ok().body(exchange);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }


}
