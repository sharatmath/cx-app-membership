package com.dev.prepaid.service;

import java.util.Map;

import com.dev.prepaid.model.invocation.InvocationRequest;

public interface InvocationService {
	
//	public Map<String, String> createHeadersMap(OMCRequest invocation);
	
//	public void callProductOnCompletionCallbackEndpoint(OMCRequest invocation, 
//			ServiceInvocationCallDTO serviceInvocationDto);
	
	public void exportProcessAndImportData(InvocationRequest invocation) throws Exception;
	
	public void processData(InvocationRequest invocation) throws Exception;
	
//	public void callProductOnCompletionCallbackEndpoint(InvocationRequest invocation);

}
