package com.viennalife.checkbin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KsmBinCheckController {

	@Autowired
	private CreditCardDetailService creditCardDetailService;

	@GetMapping("/process-all")
	public void processAllCreditCardDetails() {

		CreditCartDetail cartDetail = new CreditCartDetail();
		cartDetail.setPaymentCardNumber("4921303611023545");
		creditCardDetailService.sendRequestToOtherService(cartDetail);

	}
}
