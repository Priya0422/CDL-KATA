package com.cdl.supermarket.checkout.exception;

import static com.cdl.supermarket.checkout.constant.CheckoutConstants.*;

public class InvalidItemException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public InvalidItemException(String errorMessage) {
		super(PRICING_RULE_FAILURE +errorMessage);
	}

}
