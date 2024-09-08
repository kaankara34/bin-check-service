package com.viennalife.checkbin;

import lombok.Data;

@Data
public class BinServiceDataResponse {
	
	    private String binCode;
	    private String virtualPosId;
	    private String bankName;
	    private String dkk;
	    private String cardKind;
	    private String cardType;
	    private String bankCode;
	    private String isCommercial;
	    private String cardBrand;

}
