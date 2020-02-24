package com.praveen.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import com.praveen.config.ConfigProperties;
import com.praveen.handler.ResultHandler;

import de.commercetools.javacodingtask.client.Client;

@RunWith(JUnit4.class)
public class CSVFileProcessorTest {
	
	private CSVFileProcessor processor;
	
	@Mock
	Client client;
	
	@Before
	public void setUp() throws Exception {
		processor = new CSVFileProcessorImpl(getPropertiesObject(), new ResultHandler());
	}
	
	private ConfigProperties getPropertiesObject() {
		ConfigProperties properties = new ConfigProperties("/Users/1025537/Documents/TestData.csv", 10, "", 100);
		return properties;
	}

	@Test
	public void givenInputFileCallAPIAndProcessSucessfully() throws IOException, InterruptedException, ExecutionException {
		
		processor.processInputFile();
	}
}
