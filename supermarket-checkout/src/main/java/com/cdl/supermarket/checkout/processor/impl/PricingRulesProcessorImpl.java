package com.cdl.supermarket.checkout.processor.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.cdl.supermarket.checkout.exception.InvalidItemException;
import com.cdl.supermarket.checkout.model.Item;
import com.cdl.supermarket.checkout.model.SpecialOffer;
import com.cdl.supermarket.checkout.processor.PricingRulesProcessor;
import static com.cdl.supermarket.checkout.constant.CheckoutConstants.*;

/**
 * Class to process pricing rules and configuring item objects.
 *
 */
public class PricingRulesProcessorImpl implements PricingRulesProcessor {
	
	final static Logger logger = Logger.getLogger(PricingRulesProcessorImpl.class);
	
	private static PricingRulesProcessor pricingProcessor = null;
	private static Map<String, Item> itemDetails = new HashMap<>();
	
	private PricingRulesProcessorImpl(){
		
	}

	/**
	 * Get singleton object
	 * 
	 * @return
	 */
	public static PricingRulesProcessor getInstance() {
		if (pricingProcessor == null) {
			synchronized (PricingRulesProcessorImpl.class) {
				pricingProcessor = new PricingRulesProcessorImpl();
			}
		}
		return pricingProcessor;
	}
	
	/**
	 * This method will read input rule string and populate the item and special offer
	 * objects if the input is valid.
	 */
	@Override
	public boolean handlePricingRules(String priceRule) throws InvalidItemException {
		logger.info("Method Execution started for pricing rule process: " + priceRule);
		boolean isValidRule = false;
		boolean isValidOffer = false;
		if (priceRule != null && !priceRule.isEmpty()) {
			String[] inputStringArray = priceRule.trim().toUpperCase().split("\\|");
			try {
			isValidRule = validateItemRule(inputStringArray);
			if (isValidRule) {
				//once validate success, create Item object
				Item items = new Item();
				String Item = inputStringArray[0].trim();
				items.setItem(Item);
				items.setUnitPrice(NumberUtils.toDouble(inputStringArray[1]));
				isValidOffer = ValidateSpecialOfferRule(inputStringArray);
				if (isValidOffer) {
					SpecialOffer specialOffer = new SpecialOffer();
					String[] specialOfferArray = inputStringArray[2].trim().split("FOR");
					specialOffer.setOfferUnits(NumberUtils.toInt(specialOfferArray[0].trim()));
					specialOffer.setSpecialPrice(NumberUtils.toDouble(specialOfferArray[1].trim()));
					items.setSpecialOffer(specialOffer);
				}
				getItemDetails().put(Item, items);
			} else {
				//Invalid Input
				throw new InvalidItemException(priceRule);
			} 
			} catch (InvalidItemException e) {
				// Not re-throwing exception to continue processing and accept more user input
				logger.error(ITEM_RULE_EXCEPTION + e.getMessage(), e);
			}
		}
		logger.info("Method Execution Completed for pricing rule process: " + priceRule);
		return isValidRule;
	}

	/**
	 * Validate input string is valid
	 * @param inputStringArray
	 * @return
	 */
	private boolean validateItemRule(String[] inputStringArray) {
		logger.info("Method Execution Started - validateItemRule");
		boolean isValid = false;
		// check array has either 2 or 3 entry
		if (inputStringArray.length > 1 && inputStringArray.length < 4 
				&& NumberUtils.isParsable(inputStringArray[1].trim())) {
			//Special offer at least 3 object
			if (inputStringArray.length > 2) {
				//In third object, check the format for special offer
				if (ValidateSpecialOfferRule(inputStringArray)) {
					isValid =true;
				} else {
					isValid = false;
				}
			} else {
				isValid = true;
			}
		}
		logger.info("Method Execution Completed - validateItemRule");
		return isValid;
	}

	/**
	 * Validate Special offer 
	 * @param inputStringArray
	 * @return
	 */
	private boolean ValidateSpecialOfferRule(String[] inputStringArray) {
		logger.info("Method Execution Started - ValidateSpecialOfferRule");
		boolean isValid = false;
		//special offer valid size should be 3
		if (inputStringArray.length == 3) {
			String[] specialOfferArray = inputStringArray[2].trim().split("FOR");
			if (specialOfferArray.length == 2 && NumberUtils.isParsable(specialOfferArray[0].trim()) 
					&& NumberUtils.isParsable(specialOfferArray[1].trim())) {
				isValid =true;
			}
		}
		logger.info("Method Execution Completed - ValidateSpecialOfferRule");
		return isValid;
	}

	/**
	 * map which holds item details
	 */
	@Override
	public Map<String, Item> getItemDetails() {
		return itemDetails;
	}

}
