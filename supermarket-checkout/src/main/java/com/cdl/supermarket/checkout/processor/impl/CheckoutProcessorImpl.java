package com.cdl.supermarket.checkout.processor.impl;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.cdl.supermarket.checkout.exception.InvalidCheckoutException;
import com.cdl.supermarket.checkout.model.Cart;
import com.cdl.supermarket.checkout.model.Checkout;
import com.cdl.supermarket.checkout.model.Item;
import com.cdl.supermarket.checkout.model.SpecialOffer;
import com.cdl.supermarket.checkout.processor.CheckoutProcessor;

import static com.cdl.supermarket.checkout.constant.CheckoutConstants.*;

/**
 * This class process Item scan
 *
 */
public class CheckoutProcessorImpl implements CheckoutProcessor{
	
	final static Logger logger = Logger.getLogger(CheckoutProcessorImpl.class);
	
	private static CheckoutProcessor checkoutProcessor = null;
	
	private CheckoutProcessorImpl() {
	}
	
	public static CheckoutProcessor getInstance() {
		if (checkoutProcessor == null) {
			synchronized (CheckoutProcessorImpl.class) {
				checkoutProcessor = new CheckoutProcessorImpl();
			}
		}
		return checkoutProcessor;
		
	}

	/**
	 * Method to scan item
	 */
	@Override
	public boolean scanItem(Checkout cartCheckout, String itemName) throws InvalidCheckoutException {
		logger.info("Method Execution Started - scanItem");
		boolean scannedItem = false;
		try {
		if (itemName != null && cartCheckout != null) {
			if (cartCheckout.getCart() == null) {
				cartCheckout.setCart(new Cart());
			}
			String item = itemName.trim().toUpperCase();
			//Check Item is available in store
			if (PricingRulesProcessorImpl.getInstance().getItemDetails().containsKey(item)) {
				Map<String, Integer> itemPurchase =cartCheckout.getCart().getCheckoutItems();
				//item counts 
				if (itemPurchase.containsKey(item)) {
					itemPurchase.put(item, itemPurchase.get(item) + 1);
				} else {
					itemPurchase.put(item, 1);
				}						
				scannedItem = true;
			} else {
				throw new InvalidCheckoutException(item);
			}
		 }
		} catch (InvalidCheckoutException e) {
			logger.error(ITEM_SCAN_EXCEPTION +e.getMessage(), e);
		}
		calculateTotalAmount(cartCheckout);
		logger.info("Method Execution Completed - scanItem");
		return scannedItem;
	}

	/**
	 * Method to recalculate the amount based on all products
	 * @return
	 */
	private double calculateTotalAmount(Checkout cartCheckout) {
		logger.info("Method Execution Started - calculateTotalAmount");
		double checkoutAmount = 0.0;
		if (cartCheckout != null && cartCheckout.getCart() != null) {
			Map<String, Integer> itemPurchasedMap =cartCheckout.getCart().getCheckoutItems();
			Set<String> cartCheckoutSet = itemPurchasedMap.keySet();
			for (String item : cartCheckoutSet) {			
				Integer itemCounts = itemPurchasedMap.get(item);				
				Item items = PricingRulesProcessorImpl.getInstance().getItemDetails().get(item);
				SpecialOffer specialOffer = items.getSpecialOffer();
				int minOfferCount = 0;
				if (specialOffer != null) {
					minOfferCount = specialOffer.getOfferUnits();
					if (itemCounts >= minOfferCount) {
						checkoutAmount += (itemCounts / minOfferCount) * specialOffer.getSpecialPrice()
								+ (itemCounts % minOfferCount) * items.getUnitPrice();
					} else {
						checkoutAmount += itemCounts * items.getUnitPrice();
					}

				} else {
					checkoutAmount += itemCounts * items.getUnitPrice();
				}
				logger.debug("Checkout Amount after scanning product [" + item + "] is:"+checkoutAmount);;
			}
				cartCheckout.getCart().setTotalamount(checkoutAmount);
				logger.info("Checkout Amount " + checkoutAmount);
			}
		logger.info("Method Execution Completed - calculateTotalAmount");
		return checkoutAmount;
		}
}

