package com.viennalife.checkbin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CreditCardDetailService {

	@Autowired
	private CreditCardDetailRepository creditCardDetailRepository;
	@Autowired
	private BinServiceFeignClient binServiceFeignClient;
	@Autowired
	private KsmBinCheckRepository binCheckRepository;


	private int currentPage = 0;
	private final int pageSize = 1000; // Sayfa başına kayıt

	public void processAllRecords() {
		boolean morePages = true;

		while (morePages) {
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			Page<CreditCartDetail> tcknPage = creditCardDetailRepository.findTcknsWithNonNull(pageable);

			if (!tcknPage.isEmpty()) {
				for (CreditCartDetail dto : tcknPage) {
					// Her bir TCKN için tek tek istek gönder ve cevap al
					String response = sendRequestToOtherService(dto);
				}

				currentPage++;
			} else {
				morePages = false;
			}
		}

		currentPage = 0;
	}

	public String sendRequestToOtherService(CreditCartDetail detail) {
		// Feign client ile servise istek yap ve veriyi al
        BinServiceResponse response = binServiceFeignClient.getCardDetails(detail.getPaymentCardNumber());

        // Servis yanıtını işle
        if ("SUCCESS".equals(response.getBinServiceHeaderResponse().getStatus())) {
            logServiceResponse(response, detail.getPaymentCardNumber());
            return detail.getPaymentCardNumber() + "Tamam";
        } else {
            System.out.println("Hata oluştu: " + response.getBinServiceHeaderResponse().getMessage());
        }
        return null;
	}

	private void logServiceResponse(BinServiceResponse dto, String cardNumber) {
		KsmBinCheckLogEntity entity = new KsmBinCheckLogEntity();
		BinServiceDataResponse binServiceDataResponse = dto.getBinServiceDataResponse();
		
		entity.setBinCode(binServiceDataResponse.getBinCode());
		entity.setVirtualPosId(binServiceDataResponse.getVirtualPosId());
		entity.setBankName(binServiceDataResponse.getBankName());
		entity.setDkk(binServiceDataResponse.getDkk());
		entity.setCardType(binServiceDataResponse.getCardType());
		entity.setCardKind(binServiceDataResponse.getCardKind());
		entity.setBankCode(binServiceDataResponse.getBankCode());
		entity.setIsCommercial(binServiceDataResponse.getIsCommercial());
		entity.setCardBrand(binServiceDataResponse.getCardBrand());
		binCheckRepository.save(entity);

	}
}
