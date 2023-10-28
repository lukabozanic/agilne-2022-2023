package com.trade.tradeservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeService, Long> {

    TradeService findByFromAndTo(String from, String to);
}
