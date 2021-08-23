package com.dev.prepaid.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameters {
	private String name;
	 private String dataType;
	 private Integer width;
	 private Boolean unique;
	 private Boolean required;
	 private Boolean readOnly;
	 private Integer minimumValue;
	 private Integer maximumValue;
	 private List<String> possibleValues;
	 private String format;
	 private Object resources;
}
