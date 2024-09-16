package com.cdl.supermarket.checkout.model;

/**
 * Model to hold item details
 * 
 *
 */
public class Item {
	private String item;
	private double unitPrice;
	private SpecialOffer specialOffer;

	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public SpecialOffer getSpecialOffer() {
		return specialOffer;
	}
	public void setSpecialOffer(SpecialOffer specialOffer) {
		this.specialOffer = specialOffer;
	}
	
}
