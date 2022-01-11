/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dev.prepaid.model.MetaIndicatorsAdvanceFilter;
import com.dev.prepaid.repository.IndicatorRepository;

/**
 * @author Saket
 *
 * 
 */
@Service
public class IndicatorServiceImpl implements IndicatorService {

	@Autowired
	IndicatorRepository indicatorRepository;

	@Override
	public List<MetaIndicatorsAdvanceFilter> getAllIndicator() {
		return indicatorRepository.findAll();
	}

	@Override
	public void saveIndicator(MetaIndicatorsAdvanceFilter indicator) {

		this.indicatorRepository.save(indicator);
	}

	@Override
	public MetaIndicatorsAdvanceFilter getIndicatorById(long id) {
		Optional<MetaIndicatorsAdvanceFilter> optional = indicatorRepository.findById(id);
		MetaIndicatorsAdvanceFilter Indicator = null;
		if (optional.isPresent()) {
			Indicator = optional.get();
		} else {
			throw new RuntimeException(" Indicator not found for id :: " + id);
		}
		return Indicator;
	}

	@Override
	public void deleteIndicatorById(long id) {
		this.indicatorRepository.deleteById(id);

	}

	@Override
	public Page<MetaIndicatorsAdvanceFilter> findIndicatorPaginated(int pageNo, int pageSize, String sortField,
			String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.indicatorRepository.findAll(pageable);
	}

	@Override
	public List<MetaIndicatorsAdvanceFilter> findIndicatorByName(String functionName) {
		// TODO Auto-generated method stub
		return null;
	}

}
