package com.dev.prepaid.model;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSetDTO {
	private String id;
	 Collection < DataRowDTO > rows;
	 private Long size;
}
