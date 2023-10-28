package com.trade.tradeservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-conversion")
public interface CurrencyConversionProxy {

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    ResponseEntity<?> getConversion(@PathVariable String from, @PathVariable String to, @PathVariable double quantity, HttpServletRequest request);
}
