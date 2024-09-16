package com.viennalife.checkbin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class KsmBinCheckController {

	@Autowired
	private CreditCardDetailService creditCardDetailService;

	@GetMapping("/process-all")
	public void processAllCreditCardDetails() throws JsonMappingException, JsonProcessingException {

	
		creditCardDetailService.processAllRecords();

	}
}
