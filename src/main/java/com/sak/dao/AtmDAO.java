package com.sak.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sak.entity.AccountDetailEntity;
import com.sak.entity.LoginDetailEntity;
import com.sak.entity.UserDetailEntity;
import com.sak.repository.AccountDetailRepository;
import com.sak.repository.LoginDetailRepository;
import com.sak.repository.UserDetailRepository;

@Repository
public class AtmDAO {

	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private AccountDetailRepository accountDetailRepository;
	
	@Autowired
	private LoginDetailRepository loginDetailRepository;
	
	public Optional<UserDetailEntity> getUserDetails(String cardno) {
	Optional<UserDetailEntity>	optional =userDetailRepository.findById(cardno);
		return optional;
	}

	public Optional<AccountDetailEntity> getAccountDetails(String cardno) {
		Optional<AccountDetailEntity> optional = accountDetailRepository.findById(cardno);
		return optional;
	}

	public Integer updateBalance(String cardNumber,Integer balance) {
		return accountDetailRepository.updateBalance(cardNumber, balance);
		
	}

	public Integer updatePin(String cardNumber, Integer pin) {
		Integer count=userDetailRepository.updatePin(cardNumber, pin);
		return count;
		
		
	}

	public Optional<LoginDetailEntity> getUserloginbyid(String userId) {
		System.out.println("userId"+userId);
		Optional<LoginDetailEntity> optional=loginDetailRepository.findById(userId);
		System.out.println(optional);
		return optional;
	}

	public Optional<LoginDetailEntity> getUserById(String userId) {
		Optional<LoginDetailEntity> optional=loginDetailRepository.findById(userId);
		return optional;
	}

	public Optional<AccountDetailEntity> getCardNumber(String accountNumber) {
		Optional<AccountDetailEntity> optional= accountDetailRepository.findByAccountNumber(accountNumber);
		return optional;
	}

}
