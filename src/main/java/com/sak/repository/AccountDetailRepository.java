package com.sak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sak.entity.AccountDetailEntity;

@Repository
public interface AccountDetailRepository extends JpaRepository<AccountDetailEntity, String>{
	@Modifying
	@Query("update AccountDetailEntity e set e.balance=:balance where cardNumber=:cardNumber")
	public  Integer updateBalance(String cardNumber,Integer balance);
	
	Optional<AccountDetailEntity> findByAccountNumber(String accountNumber);
}
