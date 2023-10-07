package com.petproject.motelservice.services;

import com.petproject.motelservice.domain.payload.Email;

public interface MailService {
	
	public void sendInvoiceEmail(Email email) throws Exception;
	
}
