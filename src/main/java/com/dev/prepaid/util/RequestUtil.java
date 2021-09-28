package com.dev.prepaid.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import com.dev.prepaid.model.invocation.InvocationRequest;

public class RequestUtil {
	private static final String USERPWD       = "admin:admin";
    private static final String HEADER_KEY    = "Authorization";
    private static final String HEADER_VALUE  = "Basic ";
    private static final String SPACE         = " ";

    public static ArrayList<ClientHttpRequestInterceptor> getClientHttpInterceptorBasic(String username,String password,String contentType) {
    	String userPassword = username+":"+password;
        byte[] passwordByte           = userPassword.getBytes();
        byte[] base64passwordByte     = Base64.encodeBase64(passwordByte);
        String base64passwordString   = new String(base64passwordByte);

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
	    interceptors.add(new HeaderRequestInterceptor(HEADER_KEY, HEADER_VALUE+base64passwordString));
	    interceptors.add(new HeaderRequestInterceptor("Content-Type", contentType));
	    interceptors.add(new LoggingRequestInterceptor());
	    
        return interceptors;
    }
    
    public static ArrayList<ClientHttpRequestInterceptor> getClientHttpInterceptor(String contentType) {
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
	    interceptors.add(new HeaderRequestInterceptor("Content-Type", contentType));
	    interceptors.add(new LoggingRequestInterceptor());
	    
        return interceptors;
    }
    
    public static ArrayList<ClientHttpRequestInterceptor> getClientJWTHttpInterceptor(InvocationRequest invocation, String contentType, String token) {
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
	    interceptors.add(new HeaderRequestInterceptor("Authorization", "Bearer " + token));
	    interceptors.add(new HeaderRequestInterceptor("Content-Type", contentType));
	    interceptors.add(new HeaderRequestInterceptor("OMC-ID", invocation.getInstanceContext().getInstanceId()));
	    interceptors.add(new HeaderRequestInterceptor("INVOCATION-ID", invocation.getUuid()));
	    interceptors.add(new HeaderRequestInterceptor("DATASET-ID", invocation.getDataSet().getId()));
//	    headers.put("Authorization", "Bearer " + createJwtAuthorizationHeader(invocation, instanceContext));
//	    headers.put("Content-Type", "application/json");
//	    headers.put("OMC-ID", instanceContext.getInstanceId());
//	    headers.put("INVOCATION-ID", invocation.getUuid());
//	    headers.put("DATASET-ID", invocation.getDataSet().getId());
	    interceptors.add(new LoggingRequestInterceptor());
	    
        return interceptors;
    }
    
    public static ArrayList<ClientHttpRequestInterceptor> getClientOnCompletionCallbackJWTHttpInterceptor(InvocationRequest invocation, String contentType, String token) {
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
	    interceptors.add(new HeaderRequestInterceptor("Authorization", "Bearer " + token));
	    interceptors.add(new HeaderRequestInterceptor("Content-Type", contentType));
//	    headers.put("Authorization", "Bearer " + createJwtAuthorizationHeader(invocation, instanceContext));
//	    headers.put("Content-Type", "application/json");
//	    headers.put("OMC-ID", instanceContext.getInstanceId());
//	    headers.put("INVOCATION-ID", invocation.getUuid());
//	    headers.put("DATASET-ID", invocation.getDataSet().getId());
	    interceptors.add(new LoggingRequestInterceptor());
	    
        return interceptors;
    }
    
    public static HttpEntity<String> getPreFormattedRequestWithUserPasswordForByteHandler() {
        byte[] passwordByte           = USERPWD.getBytes();
        byte[] base64passwordByte     = Base64.encodeBase64(passwordByte);
        String base64passwordString   = new String(base64passwordByte);
        HttpHeaders httpHeaders       = new HttpHeaders();
        httpHeaders.add(HEADER_KEY,HEADER_VALUE+SPACE+base64passwordString);
        
        return new HttpEntity<>(httpHeaders);
    }
}
