package com.praveen.handler;

import java.util.concurrent.locks.StampedLock;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.springframework.stereotype.Component;

import de.commercetools.javacodingtask.client.ImportResults;

/**
 * @author PRAVEEN
 * ResultHandler class handles the results from API calls and 
 * keeps the count of Successful records and displays at the end.
 */
@Component
public class ResultHandler {
	
	private Logger logger = LogManager.getLogger(ResultHandler.class); 
	
	private final StampedLock lock = new StampedLock();
	private static int customerResultsCount =  0;
	private static int orderResultsCount = 0;

	public void customerDataResultHandler(ImportResults results) {
		long stamp = lock.writeLock();
	    try {
	    	customerResultsCount += results.getResults().size();
	    } finally {
	        lock.unlock(stamp);
	    }
	}
	
	public void orderDataResultHandler(ImportResults results) {
		long stamp = lock.writeLock();
	    try {
	    	orderResultsCount += results.getResults().size();
	    } finally {
	        lock.unlock(stamp);
	    }
	}
	
	public void displayResults() {
		logger.info("Number of Customer records from CSV file processed successfully: " + customerResultsCount);
		logger.info("Number of Order records from CSV file processed successfully: " + orderResultsCount);
	}
}
