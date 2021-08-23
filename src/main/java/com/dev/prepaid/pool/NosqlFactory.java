package com.dev.prepaid.pool;

import java.util.Properties;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.dev.prepaid.util.NosqlUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.NoSQLHandleConfig;
import oracle.nosql.driver.NoSQLHandleFactory;
import oracle.nosql.driver.iam.SignatureProvider;

@Data
@Slf4j
public class NosqlFactory extends BasePooledObjectFactory<NoSQLHandle>{

	private NosqlProperties properties;
	
	public NosqlFactory(NosqlProperties properties) {
		this.properties = properties;
	}
	
	@Override
	public NoSQLHandle create() throws Exception {	  	
    	log.debug("create handler");    	
		try {
//			String key = NosqlUtil.readKey(properties.getPkey());
//	    	System.out.println(key);
	    	
	    	SignatureProvider sp = new SignatureProvider(
	    			properties.getTenantId(),     // a string, OCID
	    			properties.getUserId(),       // a string, OCID
	    			properties.getFingerprint() , // a string
	    			properties.getKey(),
			        null
			        );
			NoSQLHandleConfig config = new NoSQLHandleConfig(properties.getEndpoint());
			config.setAuthorizationProvider(sp);
			NoSQLHandle handle = NoSQLHandleFactory.createNoSQLHandle(config);
			return handle;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
    public boolean validateObject(PooledObject<NoSQLHandle> pooledObject) {
        return pooledObject.getObject() != null;
    }
	
	@Override
    public void activateObject(PooledObject<NoSQLHandle> pooledObject) throws Exception {

		NoSQLHandle handle = pooledObject.getObject();
    }
	
	@Override
	public PooledObject<NoSQLHandle> wrap(NoSQLHandle handle) {
		return new DefaultPooledObject<>(handle);
	}
	
	// destroy the object
	@Override
	public void destroyObject(PooledObject<NoSQLHandle> p) throws Exception {
		NoSQLHandle handle = p.getObject();
		handle.close();
	}
}
