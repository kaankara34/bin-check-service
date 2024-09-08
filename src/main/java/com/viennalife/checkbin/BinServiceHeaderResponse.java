package com.viennalife.checkbin;

import java.util.List;

import lombok.Data;

@Data
public class BinServiceHeaderResponse {
	private String message;
	private String messageCode;
	private String messageDetail;
	private String status;
	private int statusCode;
	private String detailCode;
	private String txId;
	private List<String> info;

}
