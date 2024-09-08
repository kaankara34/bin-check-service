package com.viennalife.checkbin;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "credit_card_detail")
public class CreditCartDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer ksmId;

	@Column(name = "payment_card_number")
	private String paymentCardNumber;

	@Column(name = "tckn")
	private String tckn;
}
