package com.viennalife.checkbin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CreditCardDetailService {

	private static final Logger logger = LoggerFactory.getLogger(CreditCardDetailService.class);

	@Autowired
	private CreditCardDetailRepository creditCardDetailRepository;
	@Autowired
	private BinServiceFeignClient binServiceFeignClient;
	@Autowired
	private KsmBinCheckRepository binCheckRepository;

	private int currentPage = 448;
	private int recordOffset = 823;
	private final int pageSize = 1000; // Sayfa başına kayıt
	private int requestCounter = 448823; // Feign sorguları için sayaç

	public void processAllRecords() throws JsonMappingException, JsonProcessingException {
		boolean morePages = true;

		while (morePages) {
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			Page<CreditCartDetail> tcknPage = creditCardDetailRepository.findTcknsWithNonNull(pageable);

			if (!tcknPage.isEmpty()) {
				 List<CreditCartDetail> details = tcknPage.getContent();
		            if (currentPage == 182) {
		                details = details.subList(recordOffset, details.size());  // İlk 981 kaydı atla
		            }

				
				
				for (CreditCartDetail dto : details) {
					requestCounter++; // Sayaç her sorguda artacak
					logger.info("Request #{}: Processing card number: {}", requestCounter, dto.getPaymentCardNumber());
					try {
						String response = sendRequestToOtherService(dto);
						if (response != null) {
							logger.info("Successfully processed card: {}", dto.getPaymentCardNumber());
						} else {
							logger.warn("Failed to process card: {}", dto.getPaymentCardNumber());
						}
					} catch (Exception e) {
						logger.error("Error processing card: {}. Error: {}", dto.getPaymentCardNumber(),
								e.getMessage());
					}
				}

				currentPage++;
			} else {
				morePages = false;
			}
		}

		currentPage = 0;
		requestCounter = 0; // İşlem sonunda sayaç sıfırlanır
	}

	public String sendRequestToOtherService(CreditCartDetail detail)
			throws JsonMappingException, JsonProcessingException {
		// Feign client ile servise istek yap ve veriyi al
		logger.debug("Sending request #{} to BinService for card: {}", requestCounter, detail.getPaymentCardNumber());
		String response = binServiceFeignClient.getCardDetails(detail.getPaymentCardNumber());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(response);
		JsonNode dataNode = rootNode.path("data");

		String binCode = dataNode.path("binCode").asText();
		String virtualPosId = dataNode.path("virtualPosId").asText();
		String bankName = dataNode.path("bankName").asText();
		String dkk = dataNode.path("dkk").asText();
		String cardKind = dataNode.path("cardKind").asText();
		String cardType = dataNode.path("cardType").asText();
		String bankCode = dataNode.path("bankCode").asText();
		String isCommercial = dataNode.path("isCommercial").asText();
		String cardBrand = dataNode.path("cardBrand").asText();

		JsonNode headerNode = rootNode.path("header");
		String status = headerNode.path("status").asText();
		String message = headerNode.path("message").asText();

		BinServiceResponse binServiceResponse = new BinServiceResponse();
		BinServiceDataResponse binServiceDataResponse = new BinServiceDataResponse();

		binServiceDataResponse.setBankCode(bankCode);
		binServiceDataResponse.setBankName(bankName);
		binServiceDataResponse.setBinCode(binCode);
		binServiceDataResponse.setCardBrand(cardBrand);
		binServiceDataResponse.setCardKind(cardKind);
		binServiceDataResponse.setCardType(cardType);
		binServiceDataResponse.setDkk(dkk);
		binServiceDataResponse.setIsCommercial(isCommercial);
		binServiceDataResponse.setVirtualPosId(virtualPosId);

		binServiceResponse.setBinServiceDataResponse(binServiceDataResponse);

		// Servis yanıtını işle
		if ("SUCCESS".equals(status)) {
			logServiceResponse(binServiceResponse, detail.getPaymentCardNumber());
			return detail.getPaymentCardNumber() + " Tamam";
		} else {
			logger.error("Error response from BinService for card: {}. Message: {}", detail.getPaymentCardNumber(),
					message);
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
		entity.setCardNumber(cardNumber);

		binCheckRepository.save(entity);
		logger.info("Logged service response for card: {}", cardNumber);
	}
}
