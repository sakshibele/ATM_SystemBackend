package com.sak.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDetailEntity {
	@Id
	private String cardNumber ;
	
	private Integer pin ;
	private String email ;
}