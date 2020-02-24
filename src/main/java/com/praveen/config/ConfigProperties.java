package com.praveen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties {
	
	@Value("${filePath}")
    private String filePath;
	
	@Value("${thread.pool.size}")
    private int threadPoolSize;
	
	@Value("${applicant.email}")
	private String applicantEmail;
	
	@Value("${chunk.size}")
	private int chunkSize;
	
	public ConfigProperties() {}
	
	public ConfigProperties(String filePath, int threadPoolSize, String applicantEmail, int chunkSize) {
		this.filePath = filePath;
		this.threadPoolSize = threadPoolSize;
		this.applicantEmail = applicantEmail;
		this.chunkSize = chunkSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	
	public String getApplicantEmail() {
		return applicantEmail;
	}
	
	public int getChunkSize() {
		return chunkSize;
	}
	
}
