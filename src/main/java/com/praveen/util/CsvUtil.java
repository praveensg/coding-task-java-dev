package com.praveen.util;

import java.util.UUID;

public class CsvUtil {
	
	private CsvUtil() {
		
	}
	
	public static String generateRandomUuid() {
        UUID uid= UUID.randomUUID();
        return uid.toString();
    }

}
