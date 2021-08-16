package com.dev.prepaid.model.invocation;

import java.util.ArrayList;

import com.dev.prepaid.model.imports.FieldDefinitionsParameters;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataExportDTO {
	@SerializedName("fieldDefinitions") 
	private ArrayList <FieldDefinitionsParameters>  fieldDefinitions;
	@SerializedName("dataSet") 
	private DataSet dataSet;
}
