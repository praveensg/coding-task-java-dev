package com.praveen;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.praveen.service.CSVFileProcessor;


/**
 * @author PRAVEEN
 * IngestServiceApplication to process CSV file and Send data to Server using API.
 */
@SpringBootApplication
public class IngestServiceApplication implements CommandLineRunner {
	
	private Logger logger = LogManager.getLogger(IngestServiceApplication.class);
	
	@Autowired
	private CSVFileProcessor csvProcessor;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(IngestServiceApplication.class, args);
    }

    /**
     * access command line arguments
     * @param args
     */
    @Override
    public void run(String... args) throws Exception {
		try {
			logger.debug("Ingestion service started !!");
			csvProcessor.processInputFile();
			System.exit(0);
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
    }
}