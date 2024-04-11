package com.sak.service;

import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.sak.dao.AtmDAO;
import com.sak.dto.AccountDetailDTO;
import com.sak.dto.LoginDTO;
import com.sak.dto.TransactionDTO;
import com.sak.dto.UserDetailDTO;
import com.sak.entity.AccountDetailEntity;
import com.sak.entity.LoginDetailEntity;
import com.sak.entity.UserDetailEntity;
import com.sak.valueobj.BalanceObj;
import com.sak.valueobj.OTPobj;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class AtmService {

	@Autowired
	private AtmDAO atmDAO;
	
	  @Autowired
	    private MailSender mailSender;
	  
	  @Autowired
		private ModelMapper mapper;
	  
	  public void sendEmail(String to, String subject, String body) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("sakshibele0306@gmail.com");
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }
	  
	  public OTPobj generateOTP() {
		    // Generate random 4-digit OTP
		    Random random = new Random();
		    int otp = 1000 + random.nextInt(9000); // Generates random number between 1000 and 9999
		    OTPobj otpobj = new OTPobj();
		    otpobj.setOtp(otp);
		    
		    return otpobj;
		}
	
	public Boolean checkIfUserExist(String cardno) {
		Optional<UserDetailEntity>	optional=atmDAO.getUserDetails(cardno);
		if (optional.isPresent()) {
			return true;
		}
		return false;
	}

	public BalanceObj getUserBalance(String cardno) {
		Optional<AccountDetailEntity> optional = atmDAO.getAccountDetails(cardno);
		if(optional.isPresent()) {
			AccountDetailEntity accountDetailEntity = optional.get();
			BalanceObj balanceObj = new BalanceObj();
			balanceObj.setBalance(accountDetailEntity.getBalance());
			return balanceObj;
		}
		return null;
	}

	public Boolean verifyPin(String cardno, Integer pin) {
		Optional<UserDetailEntity>	optional=atmDAO.getUserDetails(cardno);
		if(optional.isPresent()) {
			UserDetailEntity userDetailEntity = optional.get();
			if(userDetailEntity.getPin().equals(pin)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	public BalanceObj withdrawAmount(TransactionDTO transactionDTO) {
		Optional<AccountDetailEntity> optional = atmDAO.getAccountDetails(transactionDTO.getCardNumber());
		if(optional.isPresent()) {
			AccountDetailEntity accDetailEntity = optional.get();
			accDetailEntity.setBalance(accDetailEntity.getBalance() - transactionDTO.getAmount());
			atmDAO.updateBalance(accDetailEntity.getCardNumber(),accDetailEntity.getBalance());
			BalanceObj balanceObj = new BalanceObj();
			balanceObj.setBalance(accDetailEntity.getBalance());
			return balanceObj;
		}
		
		return null;
	}

	public BalanceObj depositAmount(TransactionDTO transactionDTO) {
		Optional<AccountDetailEntity> optional = atmDAO.getAccountDetails(transactionDTO.getCardNumber());
		if(optional.isPresent()) {
			AccountDetailEntity accDetailEntity = optional.get();
			accDetailEntity.setBalance(accDetailEntity.getBalance() + transactionDTO.getAmount());
			Integer count=atmDAO.updateBalance(accDetailEntity.getCardNumber(),accDetailEntity.getBalance());
			System.out.println(count);
			BalanceObj balanceObj = new BalanceObj();
			balanceObj.setBalance(accDetailEntity.getBalance());
			return balanceObj;
		}
		
		return null;
	}

	public String getUserEmail(String cardno) {
		Optional<UserDetailEntity>	optional=atmDAO.getUserDetails(cardno);
		if(optional.isPresent()) {
			UserDetailEntity userDetailEntity = optional.get();
			return userDetailEntity.getEmail();
		}
		return null;
	}

	public Boolean updatePin(String cardNumber, Integer pin) {
		Integer count=atmDAO.updatePin(cardNumber,pin);
		if(count>0) {
			return true;
		}
		return false;
	}

	public Integer checkLogin(String userId, String password) {
	    Optional<LoginDetailEntity> optional = atmDAO.getUserloginbyid(userId);
	    if (optional.isPresent()) {
	    	LoginDetailEntity loginDetailEntity = optional.get();
	    	if(loginDetailEntity.getPassword().equals(password)) {
	    		return 1;
	    	}else {
	    		return 2;
	    	}
	    	
	    }
	    return 3;
	}

	public LoginDTO getUserById(String userId) {
		Optional<LoginDetailEntity> optional = atmDAO.getUserById(userId);
		if(optional.isPresent()) {
			LoginDetailEntity loginDetailEntity = optional.get();
			LoginDTO loginDTO= mapper.map(loginDetailEntity, LoginDTO.class);
			return loginDTO;
		}
		return null;
	}

	public AccountDetailDTO getCardNumber(String accountNumber) {
		Optional<AccountDetailEntity> optional = atmDAO.getCardNumber(accountNumber);
		if(optional.isPresent()) {
			AccountDetailEntity accountDetailEntity = optional.get();
			AccountDetailDTO accountDetailDTO= mapper.map(accountDetailEntity, AccountDetailDTO.class);
			return accountDetailDTO;
		}
		return null;
	}
	

	
	

}
