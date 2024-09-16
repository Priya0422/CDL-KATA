package com.cdl.supermarket.checkout.model;

/**
 * Model to hold special offer
 * 
 *
 */
public class SpecialOffer {

	private int offerUnits;
	private double specialPrice;
	
	public int getOfferUnits() {
		return offerUnits;
	}
	public void setOfferUnits(int offerUnits) {
		this.offerUnits = offerUnits;
	}
	
	public double getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(double specialPrice) {
		this.specialPrice = specialPrice;
	}
	
}
