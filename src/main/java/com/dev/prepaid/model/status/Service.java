package com.dev.prepaid.model.status;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Service {
	private Info info;
	List<Object> warnings;
	List<Object> errors;
}
