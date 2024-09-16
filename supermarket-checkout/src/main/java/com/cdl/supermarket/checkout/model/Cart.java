package com.cdl.supermarket.checkout.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model to hold checkout items and amount
 * 
 *
 */
public class Cart {
	
	private Map<String, Integer> checkoutItems = new HashMap<>();
	private double totalamount;

	public Map<String, Integer> getCheckoutItems() {
		return checkoutItems;
	}
	public void setCheckoutItems(Map<String, Integer> checkoutItems) {
		this.checkoutItems = checkoutItems;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	
	
}
