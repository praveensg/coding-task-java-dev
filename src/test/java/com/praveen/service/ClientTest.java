package com.praveen.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.commercetools.javacodingtask.client.Client;
import de.commercetools.javacodingtask.client.ImportResult;
import de.commercetools.javacodingtask.client.ImportResults;
import de.commercetools.javacodingtask.errors.ServiceUnavailableException;
import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {
	
	@Mock
	Client clientimpl;
	
	
	private List<Customer> getCustomers(){
		List<Customer> customerList = new ArrayList<Customer>();
		Customer customer1 = new Customer();
		customer1.setAge(19);
		customer1.setCity("Bangalore");
		customer1.setFirstName("John");
		customer1.setId("1");
		customer1.setLastName("Bosco");
		customer1.setState("Karnataka");
		customer1.setZip("585229");
		customerList.add(customer1);
		return customerList;
		
	}
	
	private List<Order> getOrders(){
		List<Order> orderList = new ArrayList<Order>();
		Order order1 = new Order();
		order1.setCentAmount(new BigDecimal(530));
		order1.setCurrency(Currency.getInstance("USD"));
		order1.setCustomerId("1");
		order1.setId("100");
		order1.setPick("YELLOW");
		orderList.add(order1);
		return orderList;
		
	}
	
	private ImportResults getCustomerImportResult(){
		List<ImportResult> importResultList = new ArrayList<ImportResult>();
		ImportResult importResult1 = new ImportResult();
		importResult1.setId("1");
		importResult1.setErrorCode(null);
		importResult1.setSuccess(true);
		importResultList.add(importResult1);
		ImportResults results = new ImportResults(importResultList);
		return results;
	}
	
	private ImportResults getOrderImportResult(){
		List<ImportResult> importResultList = new ArrayList<ImportResult>();
		ImportResult importResult1 = new ImportResult();
		importResult1.setId("100");
		importResult1.setErrorCode(null);
		importResult1.setSuccess(true);
		importResultList.add(importResult1);
		ImportResults results = new ImportResults(importResultList);
		return results;
	}
	
	@Test
	public void importCustomerSuccessTest() {
		
		when(clientimpl.importCustomer(getCustomers())).thenReturn(getCustomerImportResult());
		
		ImportResults importResult = clientimpl.importCustomer(getCustomers());
		Assert.assertEquals(getCustomerImportResult(), importResult);
	}
	
	
	@Test
	public void importOrdersSuccessTest() {
		
		when(clientimpl.importOrders(getOrders())).thenReturn(getOrderImportResult());
		
		ImportResults importResult = clientimpl.importOrders(getOrders());
		Assert.assertEquals(getOrderImportResult(), importResult);
	}

	@Test( expected = ServiceUnavailableException.class)
	public void importCustomerExcTest() {
		
		when(clientimpl.importCustomer(getCustomers())).thenThrow(new ServiceUnavailableException());
		clientimpl.importCustomer(getCustomers());
	}

}
