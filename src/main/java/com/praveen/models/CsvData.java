package com.praveen.models;

import java.util.List;

import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

public class CsvData {

	private List<Customer> customers;
	private List<Order> orders;
	
	public List<Customer> getCustomers() {
		return customers;
	}
	public void setCustomer(List<Customer> customers) {
		this.customers = customers;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
}
