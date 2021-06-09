package com.dev.prepaid.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PREPAID_CX_PROV_APPLICATION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxProvApplication {
	@Id
	private String id; //generate UUID
	
	private String installId; //APP INSTALL ID
	private String appId; //APP ID
	
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String uninstallBy;
	private Date uninstallDate;
}
