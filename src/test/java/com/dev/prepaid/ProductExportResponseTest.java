package com.dev.prepaid;

import com.dev.prepaid.model.invocation.ProductExportEndpointResponse;
import com.dev.prepaid.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProductExportResponseTest {
    String json ="{\n" +
            "  \"fieldDefinitions\": [\n" +
            "    {\n" +
            "      \"name\": \"APPCLOUD_ROW_CORRELATION_ID\",\n" +
            "      \"dataType\": \"Text\",\n" +
            "      \"width\": 40\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"CUSTOMER_ID_\",\n" +
            "      \"dataType\": \"Text\",\n" +
            "      \"width\": 255,\n" +
            "      \"readOnly\": true\n" +
            "    }\n" +
            "  ],\n" +
            "  \"dataSet\": {\n" +
            "    \"id\": \"0493c0d9-a954-402b-91c0-ea13b626427f\",\n" +
            "    \"size\": 2,\n" +
            "    \"rows\": [\n" +
            "      [\n" +
            "        \"139690706268;208;1632366954\",\n" +
            "        \"1234567890\"\n" +
            "      ],\n" +
            "      [\n" +
            "        \"139916262048;228;1632366954\",\n" +
            "        \"6583063914\"\n" +
            "      ]\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @Test
    public void parsingTest(){
        log.info("test parsing ProductExportEndpoint");
        ProductExportEndpointResponse response = (ProductExportEndpointResponse) GsonUtils.serializeObjectFromJSON(json, ProductExportEndpointResponse.class);
        log.info("{}", response);
    }
}
