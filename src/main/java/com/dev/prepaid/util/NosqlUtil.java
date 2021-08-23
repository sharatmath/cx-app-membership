package com.dev.prepaid.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.dev.prepaid.pool.NosqlPool;

import lombok.extern.slf4j.Slf4j;
import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ReadThrottlingException;
import oracle.nosql.driver.ops.GetRequest;
import oracle.nosql.driver.ops.GetResult;
import oracle.nosql.driver.ops.PrepareRequest;
import oracle.nosql.driver.ops.PrepareResult;
import oracle.nosql.driver.ops.PutRequest;
import oracle.nosql.driver.ops.PutResult;
import oracle.nosql.driver.ops.QueryRequest;
import oracle.nosql.driver.ops.QueryResult;
import oracle.nosql.driver.values.MapValue;

@Slf4j
public class NosqlUtil {	

    private NosqlPool pool;
    
    public NosqlUtil(NosqlPool pool) {
        this.pool = pool;
    }
	
	public static String readKey(String path) {
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	@SuppressWarnings("finally")
	public List<MapValue> select(String tableName) throws Exception {
		/*
		 * QUERY the table. The table name is inferred from the
		 * query statement.
		 */
		NoSQLHandle handle = pool.borrowObject();
		List<MapValue> results = null;
		try {
			String query = "SELECT * from " + tableName ;
			
			results = runQuery(handle,
					query);
			
			System.out.println("Number of query results for " +
					query + ": " + results.size());
			for (MapValue qval : results) {
				System.out.println("\t" + qval.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			pool.returnObject(handle);
			return results;
		}
		
	}
    	
	@SuppressWarnings("finally")
	public String get(String tableName, String id) throws Exception {
		NoSQLHandle handle = pool.borrowObject();
        GetResult getResult = null;
		try {
			MapValue key = new MapValue().put("id", id);
	        GetRequest getRequest = new GetRequest().setKey(key)
	            .setTableName(tableName);

	        getResult = handle.get(getRequest);  
	        
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnObject(handle);
			return getResult.getValue().toJson();
		}
	}
	
	@SuppressWarnings("finally")
	public Boolean put(String tableName, String data) throws Exception {
//		MapValue value = new MapValue().put("ID", 29).put("RIID", 1234);
//        PutRequest putRequest = new PutRequest().setValue(value)
//            .setTableName(tableName);

		NoSQLHandle handle = pool.borrowObject();
		PutResult putResult = null;
		try {
			
			PutRequest putRequest = new PutRequest()
					.setValueFromJson(data, null)
					.setTableName(tableName);
			
			putResult = handle.put(putRequest);  
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnObject(handle);
			if (putResult.getVersion() != null) {
				return true;
			} else {
				log.info("=== Put failed");
				return false;
			}
		}
	}
	
	public static List<MapValue> runQuery(NoSQLHandle handle,
    		String query) {
    	
    	/* A List to contain the results */
    	List<MapValue> results = new ArrayList<MapValue>();
    	
    	/*
    	 * A prepared statement is used as it is the most efficient
    	 * way to handle queries that are run multiple times.
    	 */
    	PrepareRequest pReq = new PrepareRequest().setStatement(query);
    	PrepareResult pRes = handle.prepare(pReq);
    	QueryRequest qr = new QueryRequest()
    			.setPreparedStatement(pRes.getPreparedStatement());
    	
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
