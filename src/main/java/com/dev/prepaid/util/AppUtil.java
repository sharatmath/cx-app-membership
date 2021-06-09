package com.dev.prepaid.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InstanceContext;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.model.invocation.ProductExpImpEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ReadThrottlingException;
import oracle.nosql.driver.ops.PrepareRequest;
import oracle.nosql.driver.ops.PrepareResult;
import oracle.nosql.driver.ops.QueryRequest;
import oracle.nosql.driver.ops.QueryResult;
import oracle.nosql.driver.values.MapValue;

@Slf4j
public class AppUtil {
	
	public static Long getNumberOfPages(Integer pullPageSize, Long dataSetSize) {
		   if (dataSetSize != null) {
		      if (dataSetSize >= 0) {
		         Long pages = dataSetSize / pullPageSize;
		         pages += dataSetSize % pullPageSize != 0 ? 1 : 0;
		         return pages;
		      } else {
		         throw new IllegalArgumentException("dataSize < 0");
		      }
		   } else {
		      throw new IllegalArgumentException("dataSize is null");
		   }
	}
		
	@SuppressWarnings("unchecked")
	public static DataSet exportPage(InvocationRequest invocation, ProductExpImpEndpoint productExportEndpoint, 
			Integer exportPageSize, int pageNumber, String token) throws Exception {
		   try {
//				ObjectMapper objectMapper = new ObjectMapper();
			   //call rest api
			   InstanceContext instanceContext = invocation.getInstanceContext();
			   
			   String url = productExportEndpoint.getUrl() + "?offset=" + (pageNumber * exportPageSize) + "&limit=" + exportPageSize;
			   ResponseEntity<String> responseEntity = RESTUtil.getData(invocation, token, url, null, null, "application/json");
//			   ResponseEntity<String> responseEntity = restClient.invoke(url, null, productExportEndpoint.getMethod(),
//		            headersMap);
		 
			   int statusCode = responseEntity.getStatusCodeValue();
			   if (statusCode >= 400) {
			         String msg = "Calling Product Export Endpoint: " + productExportEndpoint.getUrl()
			               + " resulted in an error status: " + statusCode;
			         log.error(msg);
			         throw new RuntimeException(msg);
			   }
		 
//			   DataSet data = objectMapper.readValue(responseEntity.getBody().toString(), DataSet.class);
			   DataSet data = (DataSet) GsonUtils.serializeObjectFromJSON(responseEntity.getBody().toString(), DataSet.class);
		 
			   List<List<String>> rows = data.getRows();
		 
			   log.info("Successful Product Export from " + productExportEndpoint.getUrl());
			   log.debug("Pulled rows[{}]", rows);
			   return data;
		   } catch (Exception e) {
			   log.error("Dataset was not properly exported, error: {}", e);
			   throw e;
		   }
	}
	
	public static List<String> stringTokenizer(String tokenizer,String delimiter){
		List<String> t2 = Collections.list(new StringTokenizer(tokenizer, delimiter)).stream()
			      .map(token -> (String) token)
			      .collect(Collectors.toList());
		return t2;
	}
	
	public static List<MapValue> runQuery(NoSQLHandle handle,
    		String query, String compartment) {
    	
    	/* A List to contain the results */
    	List<MapValue> results = new ArrayList<MapValue>();
    	
    	/*
    	 * A prepared statement is used as it is the most efficient
    	 * way to handle queries that are run multiple times.
    	 */
    	PrepareRequest pReq = new PrepareRequest().setStatement(query).setCompartment(compartment);
    	PrepareResult pRes = handle.prepare(pReq);
    	QueryRequest qr = new QueryRequest().setPreparedStatement(pRes.getPreparedStatement()).setCompartment(compartment);
    	
    	try {
    		do {
    			QueryResult res = handle.query(qr);
    			int num = res.getResults().size();
    			if (num > 0) {
    				results.addAll(res.getResults());
    			}
    			/*
    			 * Maybe add a delay or other synchronization here for
    			 * rate-limiting.
    			 */
    		} while (!qr.isDone());
    	} catch (ReadThrottlingException rte) {
    		/*
    		 * Applications need to be able to handle throttling exceptions.
    		 * The default retry handler may retry these internally. This
    		 * can result in slow performance. It is best if an application
    		 * rate-limits itself to stay under a table's read and write
    		 * limits.
    		 */
    	}
    	return results;
    }
	

}
