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

import com.dev.prepaid.model.MetaFunctionAdvanceFilter;
import com.dev.prepaid.repository.FunctionRepository;

/**
 * @author Saket
 *
 * 
 */
@Service
public class FunctionServiceImpl implements FunctionService {

	@Autowired
	FunctionRepository functionRepository;

	@Override
	public List<MetaFunctionAdvanceFilter> getAllFunction() {
		return functionRepository.findAll();
	}

	@Override
	public void saveFunction(MetaFunctionAdvanceFilter functionAdvanceFilter) {

		this.functionRepository.save(functionAdvanceFilter);
	}

	@Override
	public MetaFunctionAdvanceFilter getAdvanceFilterById(long id) {
		Optional<MetaFunctionAdvanceFilter> optional = functionRepository.findById(id);
		MetaFunctionAdvanceFilter functionAdvanceFilter = null;
		if (optional.isPresent()) {
			functionAdvanceFilter = optional.get();
		} else {
			throw new RuntimeException(" Function not found for id :: " + id);
		}
		return functionAdvanceFilter;
	}

	@Override
	public void deleteAdvanceFilterById(long id) {
		this.functionRepository.deleteById(id);

	}

	@Override
	public Page<MetaFunctionAdvanceFilter> findPaginated(int pageNo, int pageSize, String sortField,
			String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.functionRepository.findAll(pageable);
	}

	@Override
	public List<MetaFunctionAdvanceFilter> findByName(String functionName) {
		// TODO Auto-generated method stub
		return null;
	}

}
