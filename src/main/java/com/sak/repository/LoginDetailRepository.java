package com.sak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sak.entity.LoginDetailEntity;

@Repository
public interface LoginDetailRepository extends JpaRepository<LoginDetailEntity, String> {

}
