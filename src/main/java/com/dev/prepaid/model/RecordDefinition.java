package com.dev.prepaid.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RecordDefinition {
	 private ArrayList < Parameters > inputParameters;
	 private ArrayList < Parameters > outputParameters;
	 
	 public Map<String, Object> translateInputRowToMap(List<String> row) {
		 Map<String, Object> input = new HashMap<>();
		 for(int i=0;i<inputParameters.size();i++) {
			 input.put(inputParameters.get(i).getName(), row.get(i));
		 }
		 return input;		 
	 }
	 
	 public Map<String, Object> generateOutputRowAsNewMap(Map<String, Object> input) {
		 Map<String, Object> output = new HashMap<>();
		 for(int i=0;i<outputParameters.size();i++) {
			
			String name = outputParameters.get(i).getName(); 
			
	        switch(name) 
	        { 
	            case "appcloud_row_correlation_id": 
	                output.put(outputParameters.get(i).getName(), input.get("appcloud_row_correlation_id"));
	                break; 
	            default: 
					output.put(outputParameters.get(i).getName(), "");
	        } 
			 
		 }
		 return output;
	 }
}
