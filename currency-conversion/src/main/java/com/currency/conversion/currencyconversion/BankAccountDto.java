package com.currency.conversion.currencyconversion;

import java.math.BigDecimal;

public class BankAccountDto {

    private long id;

    private String email;

    private BigDecimal RSD_amount;

    private BigDecimal EUR_amount;

    private BigDecimal CHF_amount;

    private BigDecimal GBP_amount;

    private BigDecimal USD_amount;

    private String environment;


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

    public BigDecimal getRSD_amount() {
        return RSD_amount;
    }

    public void setRSD_amount(BigDecimal RSD_amount) {
        this.RSD_amount = RSD_amount;
    }

    public BigDecimal getEUR_amount() {
        return EUR_amount;
    }

    public void setEUR_amount(BigDecimal EUR_amount) {
        this.EUR_amount = EUR_amount;
    }

    public BigDecimal getCHF_amount() {
        return CHF_amount;
    }

    public void setCHF_amount(BigDecimal CHF_amount) {
        this.CHF_amount = CHF_amount;
    }

    public BigDecimal getGBP_amount() {
        return GBP_amount;
    }

    public void setGBP_amount(BigDecimal GBP_amount) {
        this.GBP_amount = GBP_amount;
    }

    public BigDecimal getUSD_amount() {
        return USD_amount;
    }

    public void setUSD_amount(BigDecimal USD_amount) {
        this.USD_amount = USD_amount;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
