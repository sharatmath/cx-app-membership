package com.dev.prepaid.model.invocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSet {
	@SerializedName("id") 
	private String id;
	@SerializedName("rows") 
	private List<List<String>> rows;
	@SerializedName("size") 
	private Long size;
}
	