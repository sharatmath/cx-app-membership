/**
 * 
 */
package com.dev.prepaid.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.prepaid.model.MetaFunctionAdvanceFilter;
import com.dev.prepaid.model.MetaIndicatorsAdvanceFilter;
import com.dev.prepaid.service.FunctionService;
import com.dev.prepaid.service.IndicatorService;
import com.dev.prepaid.util.OperationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Saket
 *
 * 
 */
@Slf4j
@Controller
@RequestMapping("/config/")
public class FctnIndicatorController {

	@Autowired
	private FunctionService functionService;

	@Autowired
	IndicatorService indicatorService;

//	Function
	@RequestMapping(value = { "getFunctionList" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> getFunctionList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = null;
		int id = OperationUtil.toInt(request.getParameter("id"), 0);
		List<MetaFunctionAdvanceFilter> functionAdvanceFilter = null;

		try {
			functionAdvanceFilter = functionService.getAllFunction();

			if (!functionAdvanceFilter.isEmpty()) {
				result.put("result", functionAdvanceFilter);
				result.put("data", "success");
			} else {
				result.put("data", "failed");
			}

		} catch (Exception e) {
			log.error("[Prepaid Membership][DataController][getFunctionList] failed!", e);
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "advConfigPage")
	public String advConfig(Model model) {
//			return "advConfig";
		return findPaginated(1, "functionName", "asc", model);
	}

	@GetMapping(value = "showFunctionForm")
	public String showFunctionForm(Model model) {
		// create model attribute to bind form data
		MetaFunctionAdvanceFilter functionAdvanceFilter = new MetaFunctionAdvanceFilter();
		model.addAttribute("functionAdvanceFilter", functionAdvanceFilter);
		return "new_function";
	}

	@PostMapping("saveFunction")
	public String saveFunction(@ModelAttribute("functionAdvanceFilter") MetaFunctionAdvanceFilter functionAdvanceFilter,
			Model model, HttpServletRequest request) {
		String functionNamess = OperationUtil.nullStringCheck(request.getParameter("functionName")).trim();
		List<MetaFunctionAdvanceFilter> functionList = null;
		MetaFunctionAdvanceFilter functionEntity = null;
		String functionName = "";

		if (functionAdvanceFilter != null) {

			functionList = functionService.getAllFunction();

			if (functionList != null && functionList.size() > 0) {
				for (int i = 0; i < functionList.size(); i++) {
					functionEntity = functionList.get(i);
//					functionName = functionEntity.getFunctionName();
					if (functionEntity.getFunctionName().equalsIgnoreCase(functionNamess)) {

						System.out.println(functionName + " already exists!");
					} else {
						functionService.saveFunction(functionAdvanceFilter);
					}
				}
			}

		}
		return findPaginated(1, "functionName", "asc", model);
	}

	@GetMapping("showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

		MetaFunctionAdvanceFilter functionAdvanceFilter = functionService.getAdvanceFilterById(id);

		model.addAttribute("functionAdvanceFilter", functionAdvanceFilter);
		return "update_function";
	}

	@GetMapping("page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 5;

		Page<MetaFunctionAdvanceFilter> page = functionService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<MetaFunctionAdvanceFilter> listFunction = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listFunction", listFunction);
		return "advConfig";
	}

//	INDICATORS
	@RequestMapping(value = { "getIndicatorList" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> getIndicatorList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = null;
		int id = OperationUtil.toInt(request.getParameter("id"), 0);
		List<MetaIndicatorsAdvanceFilter> indicatorList = null;

		try {
			indicatorList = indicatorService.getAllIndicator();

			if (!indicatorList.isEmpty()) {
				result.put("result", indicatorList);
				result.put("data", "success");
			} else {
				result.put("data", "failed");
			}

		} catch (Exception e) {
			log.error("[Prepaid Membership][DataController][indicatorList] failed!", e);
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

}
