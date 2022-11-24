package demo;

import java.util.UUID;

import demo.AccountModel.Currency;

public class PaymentModel {

	private String id;
	private String senderAccountId;
	private String receiverAccountId;
	private Currency currency;
	private double amount;
	
	public PaymentModel() {
		id = UUID.randomUUID().toString();
	}

	public PaymentModel(String id, String senderAccountId, String receiverAccountId, Currency currency, double amount) {
		this.id = id;
		this.senderAccountId = senderAccountId;
		this.receiverAccountId = receiverAccountId;
		this.currency = currency;
		this.amount = amount;
	}

	public String getSenderAccountId() {
		return senderAccountId;
	}

	public void setFromAccountId(String senderAccountId) {
		this.senderAccountId = senderAccountId;
	}

	public String getReceiverAccountId() {
		return receiverAccountId;
	}

	public void setReceiverAccountId(String receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}
	
}
