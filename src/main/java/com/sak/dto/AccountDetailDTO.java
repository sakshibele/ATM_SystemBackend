package com.sak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailDTO {
	private String cardNumber ;
	private Integer balance ;
	private String accountNumber ;
}
