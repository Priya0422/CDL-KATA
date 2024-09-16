package com.cdl.supermarket.checkout.exception;

import static com.cdl.supermarket.checkout.constant.CheckoutConstants.*;


public class InvalidCheckoutException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidCheckoutException(String errorMessage) {
		super(PRODUCT_NOT_AVAILABLE_START_MSG + errorMessage + PRODUCT_NOT_AVAILABLE_END_MSG);
	}

}
