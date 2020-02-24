package com.praveen.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

public interface CSVFileProcessor {

	void processInputFile() throws IOException, InterruptedException, ExecutionException;

	Supplier<String> mapData(List<Customer> customers, List<Order> orders);

}