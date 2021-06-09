package com.dev.prepaid.util;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    final static Logger log = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        log.debug("\n===========================request begin==============================="
        		+ "\nURI         : {}"
        		+ "\nMethod      : {}"
        		+ "\nHeaders     : {}"
        		+ "\nRequest body: {}"
        		+ "\n==========================request end===================================", 
        		request.getURI(), request.getMethod(), request.getHeaders(), new String(body, "UTF-8"));
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
//    	if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR || 
//    	          response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR || 
//    	          response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//    	            // handle SERVER_ERROR
//    		StringBuilder inputStringBuilder = new StringBuilder();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
//            String line = bufferedReader.readLine();
//            while (line != null) {
//                inputStringBuilder.append(line);
//                inputStringBuilder.append('\n');
//                line = bufferedReader.readLine();
//            }
//    		log.error("\n============================error response begin=========================="
//    				+ "\nStatus code  : {}"
//    				+ "\nStatus text  : {}"
//    				+ "\nHeaders      : {}"
//    				+ "\nResponse body: {}"
//    				+ "\n=======================error response end================================="
//            		, response.getStatusCode(), response.getStatusText(), response.getHeaders(), inputStringBuilder.toString());
//    	} 
//    	else {
//	        StringBuilder inputStringBuilder = new StringBuilder();
//	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
//	        String line = bufferedReader.readLine();
//	        while (line != null) {
//	            inputStringBuilder.append(line);
//	            inputStringBuilder.append('\n');
//	            line = bufferedReader.readLine();
//	        }
//	        log.debug("\n============================response begin=========================="
//    				+ "\nStatus code  : {}"
//    				+ "\nStatus text  : {}"
//    				+ "\nHeaders      : {}"
//    				+ "\nResponse body: {}"
//    				+ "\n=======================response end================================="
//            		, response.getStatusCode(), response.getStatusText(), response.getHeaders(), inputStringBuilder.toString());
//    	}
    }

}
