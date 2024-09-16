package com.cdl.supermarket.checkout;

import java.util.Scanner;
import org.apache.log4j.Logger;

import com.cdl.supermarket.checkout.exception.InvalidCheckoutException;
import com.cdl.supermarket.checkout.exception.InvalidItemException;
import com.cdl.supermarket.checkout.model.Checkout;
import com.cdl.supermarket.checkout.processor.CheckoutProcessor;
import com.cdl.supermarket.checkout.processor.PricingRulesProcessor;
import com.cdl.supermarket.checkout.processor.impl.CheckoutProcessorImpl;
import com.cdl.supermarket.checkout.processor.impl.PricingRulesProcessorImpl;

import static com.cdl.supermarket.checkout.constant.CheckoutConstants.*;

/*
 * Implement the code for a checkout system that handles pricing schemes such as
 * “apples cost 50 pence, three apples cost £1.30.” Implement the code for a
 * supermarket checkout that calculates the total price of a number of items. In
 * a normal supermarket, things are identified using Stock Keeping Units, or
 * SKUs. In our store, we will use individual letters of the alphabet (A, B, C,
 * and so on). Our goods are priced individually. In addition, some items are
 * multi-priced: buy ‘n’ of them, and they will cost you ‘y’ pence. For example,
 * item ‘A’ might cost 50 pence individually, but this week we have a special
 * offer: buy three ‘A’s and they will cost you £1.30.
 * 
 * A 50 3 for 130 B 30 2 for 45 C 20 D 15
 *
 *
 */
public class CheckoutApplication {

    final static Logger logger = Logger.getLogger(CheckoutApplication.class);

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        logger.info("Application Started Successfully");
        
        //To automatically close the Scanner after use
        try (Scanner scanner = new Scanner(System.in)) {
        	System.out.println("\t WELCOME TO THE STORE \n");
        	//invoking menu logic to handle checkout process
            handleCheckoutProcess(scanner);
        } catch (InvalidCheckoutException |InvalidItemException | InterruptedException e) {
            logger.error(e);
        }

        logger.info("Application completed Successfully");
    }
    
    /**
	 * Menu Options
	 * @param scanner
	 * @throws InvalidItemException
	 * @throws InvalidCheckoutException
	 * @throws InterruptedException
	 */
    private static void handleCheckoutProcess(Scanner scanner) throws InvalidCheckoutException, InvalidItemException, InterruptedException {
        boolean isApplicationRunning = true;
        while (isApplicationRunning) {
            displayOptions();
            String input = "";
            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            }
            logger.info("Key entered is " + input);
            switch (input) {
                case CASE_ONE:
                    logger.info("Pricing Rule Process Started");
                     displayPricingRules();
                     handlePricingRulesInput(scanner);
                    logger.info("Pricing Rule Process Completed");
                    break;

                case CASE_TWO:
                    logger.info("Checkout Process Started");
                    System.out.println(SCAN_ITEMS);
                    boolean success = handleItemScanProcess(scanner);
                    if(success) {
                     System.out.println(PAYMENT_INPROGRESS);
                     Thread.sleep(2000);
                     System.out.println(PAYMENT_COMPLETED);
                     Thread.sleep(2000);
                     }
                    logger.info("Checkout Process Completed");
                    break;

                case CASE_THREE:
                    logger.info("Quit Application");
                    System.out.println(APPLICATION_EXIT);
                    isApplicationRunning = false;
                    break;

                default:
                	logger.info("Default Option Executed");
                    System.out.println(INVALID_SELECTION);
                    continue;
            }
        }
    }

	/**
	 * Option details
	 */
	private static void displayOptions() {
        StringBuilder options = new StringBuilder();
        options.append(OPTION_HEADER).append(System.lineSeparator())
        .append("\t").append(OPTION_ONE).append(System.lineSeparator())
        .append("\t").append(OPTION_TWO).append(System.lineSeparator())
        .append("\t").append(OPTION_THREE).append(System.lineSeparator());
        System.out.println(options.toString());
    }
	
	/**
	 * Price rule input format
	 */
	  private static void displayPricingRules() {
			StringBuilder priceRules = new StringBuilder();
			priceRules.append("Enter Pricing rule for item in below format").append(System.lineSeparator())
			.append("<Item> | <Unit Price> | <Special Price>").append(System.lineSeparator())
			.append("Ex: A | 50 | 3 for 130").append(System.lineSeparator())
			.append("Or A | 50 [In case no offer for that item]").append(System.lineSeparator())
			.append("Once item rules are added, type DONE and press enter!");
			System.out.println(priceRules.toString());
			
		}
	  
	  /**
		 * This method read the input in [Item] | [unit price] | [special price]
		 * format <b>Ex: A | 50 | 3 for 130</b>
		 * 
		 * @param scanner
		 * @throws InvalidItemException
		 */
	  private static void handlePricingRulesInput(Scanner scanner) throws InvalidItemException {
		PricingRulesProcessor processor = PricingRulesProcessorImpl.getInstance();
		while (true) {
			String itemDetails = scanner.nextLine();
			if (itemDetails.equalsIgnoreCase(DONE)) {
				break;
			} else {
				boolean isItemAdded = processor.handlePricingRules(itemDetails);
				if (isItemAdded) {
					System.out.println(PRICING_RULE_SUCCESS);
				} else {
					System.out.println(PRICING_RULE_FAILURE + itemDetails);
				}
			}
		}
			
		}
	  
	  /**
		 * This method will scan the item and calculate the running total.
		 * 
		 * @param scanner
		 * @throws InvalidCheckoutException
		 */
		private static boolean handleItemScanProcess(Scanner scanner) throws InvalidCheckoutException {
			CheckoutProcessor processor = CheckoutProcessorImpl.getInstance();
			Checkout cartCheckout = new Checkout();
			boolean isSuccess = false;
			while (true) {
				String item = scanner.nextLine();
				if (item.equalsIgnoreCase(PAY)) {
					break;
				} else {
					isSuccess = processor.scanItem(cartCheckout, item);
					if(!isSuccess) {
						System.out.println(PRODUCT_NOT_AVAILABLE_START_MSG +item +PRODUCT_NOT_AVAILABLE_END_MSG);
					}
					String totalAmnount = CheckoutProcessor.getTotalAmount(cartCheckout);
					System.out.println(AMOUNT +totalAmnount +ITEM_SCAN_MSG );
				}
			}
			System.out.println(TOTAL_AMOUNT + CheckoutProcessor.getTotalAmount(cartCheckout));
			return isSuccess;
		}
}
