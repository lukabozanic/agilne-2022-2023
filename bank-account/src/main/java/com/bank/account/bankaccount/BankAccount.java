package com.bank.account.bankaccount;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(unique=true)
	private String email;

	@Column
	private BigDecimal RSD_amount;
	
	@Column
	private BigDecimal EUR_amount;
	
	@Column
	private BigDecimal CHF_amount;
	
	@Column
	private BigDecimal GBP_amount;
	
	@Column
	private BigDecimal USD_amount;
	
	@Transient
	private String environment;

	public BankAccount() {
	
	}

	public BankAccount(String email, String environment) {
		super();
		this.email = email;
		RSD_amount = BigDecimal.valueOf(0);
		EUR_amount = BigDecimal.valueOf(0);
		CHF_amount = BigDecimal.valueOf(0);
		GBP_amount = BigDecimal.valueOf(0);
		USD_amount = BigDecimal.valueOf(0);
		this.environment = environment;
	}
	
	public BankAccount(long id, String email, BigDecimal rSD_amount, BigDecimal eUR_amount, BigDecimal cHF_amount,
			BigDecimal gBP_amount, BigDecimal uSD_amount, String environment) {
		super();
		this.id = id;
		this.email = email;
		RSD_amount = rSD_amount;
		EUR_amount = eUR_amount;
		CHF_amount = cHF_amount;
		GBP_amount = gBP_amount;
		USD_amount = uSD_amount;
		this.environment = environment;
	}
	
	public BankAccount(BigDecimal rSD_amount, BigDecimal eUR_amount, BigDecimal cHF_amount,
			BigDecimal gBP_amount, BigDecimal uSD_amount) {
		super();
		RSD_amount = rSD_amount;
		EUR_amount = eUR_amount;
		CHF_amount = cHF_amount;
		GBP_amount = gBP_amount;
		USD_amount = uSD_amount;
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

	public BigDecimal getRSD_amount() {
		return RSD_amount;
	}

	public void setRSD_amount(BigDecimal rSD_amount) {
		RSD_amount = rSD_amount;
	}

	public BigDecimal getEUR_amount() {
		return EUR_amount;
	}

	public void setEUR_amount(BigDecimal eUR_amount) {
		EUR_amount = eUR_amount;
	}

	public BigDecimal getCHF_amount() {
		return CHF_amount;
	}

	public void setCHF_amount(BigDecimal cHF_amount) {
		CHF_amount = cHF_amount;
	}

	public BigDecimal getGBP_amount() {
		return GBP_amount;
	}

	public void setGBP_amount(BigDecimal gBP_amount) {
		GBP_amount = gBP_amount;
	}

	public BigDecimal getUSD_amount() {
		return USD_amount;
	}

	public void setUSD_amount(BigDecimal uSD_amount) {
		USD_amount = uSD_amount;
	}
	
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
}
