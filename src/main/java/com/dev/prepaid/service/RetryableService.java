package com.dev.prepaid.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.model.invocation.ProductExpImpEndpoint;

import java.util.List;

public interface RetryableService {
	
	@Retryable(value = { Exception.class }, maxAttempts = 60, backoff = @Backoff(delay = 5000))  //5sec ,60times
	public DataSet exportPage(InvocationRequest invocation, ProductExpImpEndpoint productExportEndpoint, 
			Integer exportPageSize, int pageNumber) throws Exception;

	@Retryable(value = { Exception.class }, maxAttempts = 2, backoff = @Backoff(delay = 5000))  //5sec ,2times
	public void callProductOnCompletionCallbackEndpoint(InvocationRequest invocation) throws Exception;

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 10000))  //10sec ,3times
	public void callProductImportEndpoint(List<List<String>> rows, InvocationRequest invocation) throws Exception;


	@Recover
    public String getBackendResponseFallback(Exception e);
}
