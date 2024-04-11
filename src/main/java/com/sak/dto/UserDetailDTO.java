package com.sak.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {
	private String cardNumber ;	
	private Integer pin ;
	private String email ;
}
