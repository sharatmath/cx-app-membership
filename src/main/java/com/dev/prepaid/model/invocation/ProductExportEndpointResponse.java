package com.dev.prepaid.model.invocation;


import com.dev.prepaid.model.RecordDefinition;
import com.dev.prepaid.model.imports.FieldDefinitionsParameters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductExportEndpointResponse {
    private ArrayList<FieldDefinitionsParameters> fieldDefinitions;
    private DataSet dataSet;
}
