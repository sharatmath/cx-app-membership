package com.dev.prepaid.service;

import java.util.Map;

import com.dev.prepaid.model.invocation.InvocationRequest;

public interface InvocationService {
	public void processData(InvocationRequest invocation) throws Exception;
}
