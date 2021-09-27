package com.dev.prepaid.service;

import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.model.invocation.ProductExportEndpointResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public interface InvocationService {
	public void invoke(InvocationRequest invocation) throws  Exception;
	public void processData(InvocationRequest invocation, InvocationRequest invocationOri) throws Exception;
	@Retryable(value = { Exception.class }, maxAttempts = 1, backoff = @Backoff(delay = 5000))  //5sec ,2times
	public ProductExportEndpointResponse productExportEndpoint(InvocationRequest invocation, int limit, int offset) throws Exception;

	@Retryable(value = { Exception.class }, maxAttempts = 1, backoff = @Backoff(delay = 5000))  //5sec ,2times
	public void productImportEndpoint(InvocationRequest invocation) throws Exception;

	@Retryable(value = { Exception.class }, maxAttempts = 2, backoff = @Backoff(delay = 5000))  //5sec ,2times
	public void onCompletionCallbackEndpoint(InvocationRequest invocation) throws Exception;
}
