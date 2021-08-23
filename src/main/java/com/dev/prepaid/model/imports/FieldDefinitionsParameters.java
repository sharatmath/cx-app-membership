package com.dev.prepaid.model.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinitionsParameters {
	private String name;
	private String dataType;
	private float width;
	private boolean readOnly;
}
