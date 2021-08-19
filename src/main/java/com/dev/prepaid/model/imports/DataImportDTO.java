package com.dev.prepaid.model.imports;

import java.util.ArrayList;

import com.dev.prepaid.model.Parameters;
import com.dev.prepaid.model.invocation.DataSet;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataImportDTO {
	@SerializedName("fieldDefinitions") 
	private ArrayList <Parameters>  fieldDefinitions;
	@SerializedName("dataSet") 
	private DataSet dataSet;
}
