package com.dev.prepaid.service;

import java.util.List;
import java.util.Map;

import com.dev.prepaid.domain.PrepaidCxProvInstances;
import com.dev.prepaid.model.DataRowDTO;
import com.dev.prepaid.model.invocation.InstanceContext;
import com.dev.prepaid.model.invocation.InvocationRequest;

public interface PrepaidAppService {
	
	public List<DataRowDTO> processData(
			List<List<String>> rows, 
			InvocationRequest invocation, 
			PrepaidCxProvInstances instanceConfiguration, 
			String groupId,
			Long dataSetSize);

}
