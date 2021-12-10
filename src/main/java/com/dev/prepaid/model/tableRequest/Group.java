/**
 * 
 */
package com.dev.prepaid.model.tableRequest;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Saket
 *
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

	private String groupId;
	private Boolean rootGroup;
	private String groupCondition;
	private List<Group> groups;
	private List<DataList> dataList;
}
