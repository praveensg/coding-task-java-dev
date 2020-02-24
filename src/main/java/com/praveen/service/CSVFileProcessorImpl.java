package com.praveen.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.praveen.config.ConfigProperties;
import com.praveen.handler.ResultHandler;
import com.praveen.models.CsvData;
import com.praveen.util.CsvUtil;

import de.commercetools.javacodingtask.client.Client;
import de.commercetools.javacodingtask.client.ClientFactory;
import de.commercetools.javacodingtask.client.ImportResults;
import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

/**
 * @author PRAVEEN
 * Class is responsible for processing CSV data in chunks of 10,000 
 * lines and invokes customer and order API client methods.
 */
@Service
public class CSVFileProcessorImpl implements CSVFileProcessor {

	private static final String USD = "USD";
	private static final String CUSTOMER_ID = "customerId";
	private static final String COMMA = ",";
	
	private Logger logger = LogManager.getLogger(CSVFileProcessorImpl.class);
	
	@Autowired
	private ConfigProperties properties;
	
	@Autowired
	private ResultHandler handler;
	
	public CSVFileProcessorImpl() {}
	
	public CSVFileProcessorImpl(ConfigProperties properties, ResultHandler handler) {
		this.properties = properties;
		this.handler = handler;
	}
	
	/**
	 * Reads the CSV file line by line,
	 * Create threads and assign to process the request
	 * @param filePath the location of the file to process
	 */
	@Override
	public void processInputFile() throws IOException, InterruptedException, ExecutionException {
		try {
        	ExecutorService executorService = Executors.newFixedThreadPool(properties.getThreadPoolSize());
        	logger.debug("Executor Service is created with " + properties.getThreadPoolSize() + "threads.");
            List<CompletableFuture<String>> futures = new ArrayList<>();
            CsvData csvData = new CsvData();
            csvData.setCustomer(new ArrayList<Customer>());
            csvData.setOrders(new ArrayList<Order>());
            Files.lines(Paths.get(properties.getFilePath()))
            .filter(line -> StringUtils.isNotBlank(line) && !line.trim().startsWith(CUSTOMER_ID))
            .forEach(line->{
            	mapToObject(line, csvData);
                if(csvData.getCustomers().size() % properties.getChunkSize() == 0) {
                    addTasksToFuture(executorService, futures, csvData);
                    csvData.getCustomers().clear();
                    csvData.getOrders().clear();
                }
            });
            if(csvData.getCustomers().size()>0) {
                addTasksToFuture(executorService, futures, csvData);
                csvData.getCustomers().clear();
                csvData.getOrders().clear();
            }
            
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
            .get();
            
            Map<Boolean, List<CompletableFuture<String>>> result = 
            		futures.stream().collect(Collectors.partitioningBy(CompletableFuture::isCompletedExceptionally));
            
            logger.info("Failed API calls due to Exceptions: " + result.get(false).size());
            logger.info("Successful API calls: " + result.get(true).size());
            
            handler.displayResults();
            
            logger.info("CSV file Process completed!!");
            executorService.shutdown();
            
        } catch (IOException e) {
            logger.error("File read issues.", e);
        }
    }

	private void addTasksToFuture(ExecutorService executorService, List<CompletableFuture<String>> futures, CsvData csvData) {
		futures.add(CompletableFuture.supplyAsync(
				mapData(new ArrayList<Customer>(csvData.getCustomers()), new ArrayList<Order>(csvData.getOrders())), executorService)
				.exceptionally(exception -> {
					logger.error(exception);
					return "ERROR";
				}));
	}
	
	
	/**
	 * @param customers
	 * @param orders
	 * @return Supplier<String>
	 */
	public Supplier<String> mapData(List<Customer> customers, List<Order> orders){
        return ()->{
        	return processRecord(customers, orders);
        	};
    }

	/**
	 * Calls the client methods with list of Customers and Orders
	 * @param customers
	 * @param orders
	 * @return
	 */
	private String processRecord(List<Customer> customers, List<Order> orders) {
		Client client = ClientFactory.create(properties.getApplicantEmail());
		ImportResults customerResults = client.importCustomer(customers);
		ImportResults orderResults = client.importOrders(orders);
		
		handler.customerDataResultHandler(customerResults);
		handler.orderDataResultHandler(orderResults);
		
        return "SUCCESS";
	}
	
	/**
	 * @param line
	 * @param csvData
	 * @return csvData object
	 */
	private CsvData mapToObject(String line, CsvData csvData) {
		String[] csvLine = line.split(COMMA);
		Customer customer = mapToCustomer(csvLine);
		if(csvData.getCustomers() == null) {
			csvData.setCustomer(new ArrayList<Customer>());
		}
		csvData.getCustomers().add(customer);

		Order order = mapToOrder(csvLine);
		if(csvData.getOrders() == null) {
			csvData.setOrders(new ArrayList<Order>());
		}
		csvData.getOrders().add(order);

		return csvData;
	}

	/**
	 * Map csvLine data to Customer Object
	 * @param csvLine
	 * @return Customer object
	 */
	private Customer mapToCustomer(String[] csvLine) {
		Customer customer = new Customer();
		customer.setId(csvLine[0]);
		customer.setFirstName(csvLine[1]);
		customer.setLastName(csvLine[2]);
		String age = csvLine[3];
		try { 
			int a = Integer.parseInt(age);
			customer.setAge(a);
		} catch(NumberFormatException ne) {
			logger.debug("Could not parse age value to Integer.", ne);
		}
		customer.setStreet(csvLine[4]);
		customer.setCity(csvLine[5]);
		customer.setState(csvLine[6]);
		customer.setZip(csvLine[7]);
		return customer;
	}
	
	/**
	 * Map csvLine data to Order Object 
	 * Generate Id for Order
	 * @param csvLine
	 * @return Order object
	 */
	private Order mapToOrder(String[] csvLine) {
		Order order = new Order();
		order.setId(CsvUtil.generateRandomUuid());
		order.setCustomerId(csvLine[0]);
		order.setCurrency(Currency.getInstance(USD));
		try { 
			BigDecimal amount = new BigDecimal(csvLine[8].substring(1));
			order.setCentAmount(amount);
		} catch(NumberFormatException ne) {
			logger.debug("Could not parse dollar amount.", ne);
		}
		order.setPick(csvLine[9]);
		return order;
	}
	

}
