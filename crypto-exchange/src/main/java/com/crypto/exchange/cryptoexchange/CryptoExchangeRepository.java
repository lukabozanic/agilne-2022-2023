package com.crypto.exchange.cryptoexchange;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoExchangeRepository extends JpaRepository<CryptoExchange, Long> {

    CryptoExchange findByFromAndTo(String from, String to);
}