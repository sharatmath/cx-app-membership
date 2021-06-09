package com.dev.prepaid.repository.nosql;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.dev.prepaid.domain.nosql.PrepaidCxProvInvocations;
import com.dev.prepaid.pool.NosqlPool;
import com.dev.prepaid.util.AppUtil;
import com.dev.prepaid.util.GsonUtils;

import lombok.extern.slf4j.Slf4j;
import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ops.PutRequest;
import oracle.nosql.driver.ops.PutResult;
import oracle.nosql.driver.values.MapValue;

@Slf4j
public class PrepaidCxProvInvocationsRepository {
	/* Name of your table */
    private static final String tableName = "PREPAID_CX_PROV_INVOCATIONS";
    
    @Value("${no.sql.compartment}")
	private String compartment;

	private NosqlPool pool;
    
    public PrepaidCxProvInvocationsRepository(NosqlPool pool) {
        this.pool = pool;
    }
	
    
    @SuppressWarnings("finally")
	public PrepaidCxProvInvocations findOneById(String id) throws Exception {
    	NoSQLHandle handle = pool.borrowObject();
		List<MapValue> results = null;
		PrepaidCxProvInvocations prepaidCxProvInvocations = null;
		try {
			String query = "SELECT * FROM " + tableName + " WHERE ID = '" + id + "' LIMIT 1";
			
			results = AppUtil.runQuery(handle, query, compartment);			
			for (MapValue qval : results) {
				prepaidCxProvInvocations = (qval != null) 
						? (PrepaidCxProvInvocations) GsonUtils.serializeObjectFromJSON(qval.toJson(), PrepaidCxProvInvocations.class) 
						: null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.returnObject(handle);
			return prepaidCxProvInvocations;
		}
	}
    
    @SuppressWarnings("finally")
	public Boolean save(PrepaidCxProvInvocations object) throws Exception {
		NoSQLHandle handle = pool.borrowObject();
		PutResult putResult = null;
		try {
			String data = GsonUtils.deserializeObjectToJSON(object);
			
			PutRequest putRequest = new PutRequest()
					.setValueFromJson(data, null)
					.setTableName(tableName)
					.setCompartment(compartment);
			
			putResult = handle.put(putRequest);  
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnObject(handle);
			if (putResult.getVersion() != null) {
				return true;
			} else {
				return false;
			}
		}
	}
}
