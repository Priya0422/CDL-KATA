package com.cdl.supermarket.checkout.processor;


import java.util.Currency;
import java.util.Locale;

import com.cdl.supermarket.checkout.exception.InvalidCheckoutException;
import com.cdl.supermarket.checkout.model.Checkout;

public interface CheckoutProcessor {

	/**
	 * Override to implement Item scan logic
	 * 
	 * @param item
	 * @return
	 * @throws InvalidCheckoutException
	 */
	boolean scanItem(Checkout cartCheckout, String item) throws InvalidCheckoutException;

	/**
	 * method to display amount.
	 * 
	 * @param cartCheckout
	 * @return
	 */
	static public String getTotalAmount(Checkout cartCheckout) {
		double amount = 0.0;
		if (cartCheckout != null && cartCheckout.getCart() != null) {
			amount = cartCheckout.getCart().getTotalamount();
		}
		//Format the amount as a currency string with the currency symbol
		Currency myCurrency = Currency.getInstance(Locale.UK);
	    String currencySymbol = myCurrency.getSymbol(Locale.UK);
		String totalAmount = currencySymbol + (amount/100);
		return totalAmount;
	}

}
