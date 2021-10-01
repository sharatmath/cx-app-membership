package com.dev.prepaid.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.model.invocation.ProductExpImpEndpoint;

public interface RetryableService {
	
	@Retryable(value = { Exception.class }, maxAttempts = 60, backoff = @Backoff(delay = 5000))  //5sec ,60times
	public DataSet exportPage(InvocationRequest invocation, ProductExpImpEndpoint productExportEndpoint, 
			Integer exportPageSize, int pageNumber) throws Exception;

	@Retryable(value = { Exception.class }, maxAttempts = 60, backoff = @Backoff(delay = 5000))  //5sec ,60times
	public void callProductOnCompletionCallbackEndpoint(InvocationRequest invocation) throws Exception;

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))  //5sec ,60times
	public void callProductImportEndpoint(InvocationRequest invocation) throws Exception;


	@Recover
    public String getBackendResponseFallback(Exception e);
}
