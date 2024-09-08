package com.viennalife.checkbin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "binServiceFeignClient", url = "${bin.service.url}")
public interface BinServiceFeignClient {

	@GetMapping("/bins/info")
	BinServiceResponse getCardDetails(@RequestParam("paymentCard") String paymentCard);
}
