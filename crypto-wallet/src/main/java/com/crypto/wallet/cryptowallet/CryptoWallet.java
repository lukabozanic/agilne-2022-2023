package com.crypto.wallet.cryptowallet;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CryptoWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String email;

    @Column
    private BigDecimal BTC_amount;

    @Column
    private BigDecimal ETH_amount;

    @Column
    private BigDecimal LTC_amount;

    @Column
    private BigDecimal XRP_amount;

    @Transient
    private String environment;

    public CryptoWallet() {

    }

    public CryptoWallet(String email, BigDecimal bTC_amount, BigDecimal eTH_amount, BigDecimal lTC_amount,
                        BigDecimal xRP_amount, String environment) {
        super();
        this.email = email;
        BTC_amount = bTC_amount;
        ETH_amount = eTH_amount;
        LTC_amount = lTC_amount;
        XRP_amount = xRP_amount;
        this.environment = environment;
    }

    public CryptoWallet(BigDecimal bTC_amount, BigDecimal eTH_amount, BigDecimal lTC_amount,
                        BigDecimal xRP_amount) {
        super();
        BTC_amount = bTC_amount;
        ETH_amount = eTH_amount;
        LTC_amount = lTC_amount;
        XRP_amount = xRP_amount;
    }

    public CryptoWallet(String email,  String environment) {
        super();
        this.email = email;
        BTC_amount = BigDecimal.valueOf(0);
        ETH_amount = BigDecimal.valueOf(0);
        LTC_amount = BigDecimal.valueOf(0);
        XRP_amount = BigDecimal.valueOf(0);
        this.environment = environment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBTC_amount() {
        return BTC_amount;
    }

    public void setBTC_amount(BigDecimal bTC_amount) {
        BTC_amount = bTC_amount;
    }

    public BigDecimal getETH_amount() {
        return ETH_amount;
    }

    public void setETH_amount(BigDecimal eTH_amount) {
        ETH_amount = eTH_amount;
    }

    public BigDecimal getLTC_amount() {
        return LTC_amount;
    }

    public void setLTC_amount(BigDecimal lTC_amount) {
        LTC_amount = lTC_amount;
    }

    public BigDecimal getXRP_amount() {
        return XRP_amount;
    }

    public void setXRP_amount(BigDecimal xRP_amount) {
        XRP_amount = xRP_amount;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}