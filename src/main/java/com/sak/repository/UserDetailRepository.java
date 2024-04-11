package com.sak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.sak.entity.UserDetailEntity;

public interface UserDetailRepository extends JpaRepository<UserDetailEntity, String>{
	@Modifying
	@Query("update UserDetailEntity e set e.pin=:pin where cardNumber=:cardNumber")
	public  Integer updatePin(String cardNumber,Integer pin);
}
