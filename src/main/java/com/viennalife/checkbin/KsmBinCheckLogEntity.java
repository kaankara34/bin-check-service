package com.viennalife.checkbin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ksm_bin_check")
public class KsmBinCheckLogEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ksm_id")
    private Integer ksmId;

    @Column(name = "bin_code")
    private String binCode;

    @Column(name = "virtual_pos_id")
    private String virtualPosId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "dkk")
    private String dkk;

    @Column(name = "card_kind")
    private String cardKind;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "is_commercial")
    private String isCommercial;

    @Column(name = "card_brand")
    private String cardBrand;
    @Column(name = "card_number")
    private String cardNumber;

}
