package com.dev.prepaid.model.tableRequest;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectedColumn {
    @SerializedName("af_column_id")
    private String afColumnId;

    @SerializedName("af_tables_id")
    private String afTablesId;

    @SerializedName("column_name")
    private String columnName;
    @SerializedName("column_type")
    private LocalDateTime columnType;

    @SerializedName("last_modified_date")
    private LocalDateTime lastModifiedDate;

}
