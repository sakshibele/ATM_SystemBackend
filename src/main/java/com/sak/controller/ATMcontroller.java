package com.sak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sak.dto.AccountDetailDTO;
import com.sak.dto.LoginDTO;
import com.sak.dto.TransactionDTO;
import com.sak.dto.UserDetailDTO;
import com.sak.entity.LoginDetailEntity;
import com.sak.service.AtmService;
import com.sak.valueobj.BalanceObj;
import com.sak.valueobj.OTPobj;


@CrossOrigin
@RestController
@RequestMapping("/sakshi")
public class ATMcontroller {

	@Autowired
	private AtmService atmService;
	
//	@RequestMapping(method = { RequestMethod.POST,
//			RequestMethod.GET }, value = "/checkifexist/{cardno}")
	
	@RequestMapping(method = RequestMethod.GET, value = "/checkifexists/{cardno}")
	public Boolean checkIfExist(@PathVariable String cardno) {
		
		return atmService.checkIfUserExist(cardno);
	}
	
	
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },method = { RequestMethod.POST,
			RequestMethod.GET }, value = "/verify")
	public Boolean verifyPin(@RequestBody UserDetailDTO userDetailDTO) {
		System.out.println(userDetailDTO);
		
		return atmService.verifyPin(userDetailDTO.getCardNumber(),userDetailDTO.getPin());
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },method = { RequestMethod.POST,
			RequestMethod.GET }, value = "/generatepin")
	public Boolean updatePin(@RequestBody UserDetailDTO userDetailDTO) {
		System.out.println(userDetailDTO);
		
		return atmService.updatePin(userDetailDTO.getCardNumber(),userDetailDTO.getPin());
	}
	
//	@RequestMapping(method = { RequestMethod.POST,
//			RequestMethod.GET }, value = "/withdrawammount")
//	public Integer withdrawAmount(@RequestBody AccountDetailDTO accountDetailDTO, Integer amount) {
//		Integer balance=atmService.withdrawAmount(accountDetailDTO,amount);
//		
//		return balance;
//	}
	
	@GetMapping(value = "/getbalance/{cardno}")
	public BalanceObj getUserBalance(@PathVariable String cardno) {
		
		return atmService.getUserBalance(cardno);
	}
	
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.GET }, value = "/withdrawamount")
	public BalanceObj withdrawAmount(@RequestBody TransactionDTO transactionDTO) {
		BalanceObj balanceObj = atmService.getUserBalance(transactionDTO.getCardNumber());
		Integer balance= balanceObj.getBalance();
		Integer userBalance = transactionDTO.getAmount();
		
		if(balance<userBalance) {
			return null;
		}
		return atmService.withdrawAmount(transactionDTO);
	}
	
	
	
//	public BalanceObj withdrawAmount(@RequestBody TransactionDTO transactionDTO) {
//		BalanceObj balanceObj = atmService.getUserBalance(transactionDTO.getCardNumber());
//		Integer balance= balanceObj.getBalance();
//		Integer userBalance = transactionDTO.getAmount();
//		
//		if(balance<userBalance) {
//			return null;
//		}
//		return atmService.withdrawAmount(transactionDTO);
//	}
	
//	@RequestMapping(method = { RequestMethod.POST,
//			RequestMethod.GET }, value = "/depositammount")
//	public Integer depositAmount(@RequestBody TransactionDTO transactionDTO) {
//		Integer balance=atmService.depositAmount(transactionDTO);
//		
//		return balance;
//	}
	
	
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/depositamount")
	public BalanceObj depositAmount(@RequestBody TransactionDTO transactionDTO) {
	   
	    return atmService.depositAmount(transactionDTO);
	}
	
	
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/generateotp/{cardno}")
	public OTPobj generateOtp(@PathVariable String cardno) {
		OTPobj otp = atmService.generateOTP();
        String userEmail = atmService.getUserEmail(cardno);
	    // Compose email subject and body
	    String subject = "OTP Verification";
	    String body = "Your OTP is: " + otp;

	    // Send email
	    atmService.sendEmail(userEmail, subject, body);

	    // Return the OTP for verification at the client side
	    return otp;
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/userlogin")
	public Integer checkLogin(@RequestBody LoginDTO loginDTO) {
		
		
		return atmService.checkLogin(loginDTO.getUserId(),loginDTO.getPassword());
	}
//	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/userlogin")
//	public Integer checkLogin(@PathVariable String userId, @PathVariable String password) {
//		
//		
//		return atmService.checkLogin(userId,password);
//	}
	
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/getuserbalance/{userId}")
	public BalanceObj getBalanceInLogin(@PathVariable String userId) {
		LoginDTO loginDTO = atmService.getUserById(userId);
		String accountNumber=loginDTO.getAccountNumber();
		System.out.println(accountNumber);
		AccountDetailDTO accountDetailDTO = atmService.getCardNumber(accountNumber);
		return atmService.getUserBalance(accountDetailDTO.getCardNumber());
	}
	
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/fundtransfer/{userId}/{amount}/{receiverAccountNumber}")
	public Integer fundTransfer(@PathVariable String userId,@PathVariable Integer amount, @PathVariable String receiverAccountNumber) {
		AccountDetailDTO accountDetailDTO1 = atmService.getCardNumber(receiverAccountNumber);//receiver

		if(accountDetailDTO1!=null) {
			LoginDTO loginDTO = atmService.getUserById(userId);
			String senderAccountNumber=loginDTO.getAccountNumber();
			
		   //to check if receiver's account no is correct
			AccountDetailDTO accountDetailDTO2 = atmService.getCardNumber(senderAccountNumber);//sender
			
			BalanceObj balanceObj = atmService.getUserBalance(accountDetailDTO2.getCardNumber());
			Integer balance= balanceObj.getBalance();
			
			
			if(balance>amount) {
				
				TransactionDTO transactionDTO1 = new TransactionDTO(); //withdraw money from sender
				transactionDTO1.setCardNumber(accountDetailDTO2.getCardNumber());
				transactionDTO1.setAmount(amount);
				
				BalanceObj balanceObj2=withdrawAmount(transactionDTO1);
				Integer remainingBalance=balanceObj2.getBalance();
				System.out.println("remainingBalance of sender"+remainingBalance);
				
				
				
				TransactionDTO transactionDTO2 = new TransactionDTO();//deposit money to receiver
				transactionDTO2.setCardNumber(accountDetailDTO1.getCardNumber());
				transactionDTO2.setAmount(amount);
				
				BalanceObj balanceObj3=depositAmount(transactionDTO2);
				Integer afterdBalance = balanceObj3.getBalance();
				System.out.println("Final balance of receiver"+afterdBalance);
				
				
				return 1;
			}else {
				return 2;
			}
			
		}else {
			return 3;
		}
		
		
	}
	
}
