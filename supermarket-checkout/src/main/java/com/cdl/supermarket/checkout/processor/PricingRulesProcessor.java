package com.cdl.supermarket.checkout.processor;

import java.util.Map;

import com.cdl.supermarket.checkout.exception.InvalidItemException;
import com.cdl.supermarket.checkout.model.Item;

public interface PricingRulesProcessor {
	
	/**
	 * Override to implement pricing rule for input
	 * @param priceRule
	 * @return
	 * @throws InvalidItemException
	 */
	boolean handlePricingRules(String priceRule) throws InvalidItemException ;
	
	
	/**
	 * Map to hold Item details
	 * @return
	 */
	Map<String, Item> getItemDetails();

}
