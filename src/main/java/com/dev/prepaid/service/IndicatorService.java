/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dev.prepaid.model.MetaIndicatorsAdvanceFilter;

/**
 * @author Saket
 *
 * 
 */
public interface IndicatorService {
	List<MetaIndicatorsAdvanceFilter> getAllIndicator();

	void saveIndicator(MetaIndicatorsAdvanceFilter indicator);

	MetaIndicatorsAdvanceFilter getIndicatorById(long id);

	void deleteIndicatorById(long id);

	Page<MetaIndicatorsAdvanceFilter> findIndicatorPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	List<MetaIndicatorsAdvanceFilter> findIndicatorByName(String functionName);

}
