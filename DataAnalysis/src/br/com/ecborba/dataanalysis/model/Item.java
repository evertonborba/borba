package br.com.ecborba.dataanalysis.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Item {

	private BigInteger itemId;
	private BigInteger itemQuantity;
	private BigDecimal itemPrice;
	private BigDecimal itemTotal;

	public BigInteger getItemId() {
		return itemId;
	}

	public void setItemId(BigInteger itemId) {
		this.itemId = itemId;
	}

	public BigInteger getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(BigInteger itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public BigDecimal getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(BigDecimal itemTotal) {
		this.itemTotal = itemTotal;
	}

}
