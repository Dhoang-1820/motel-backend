package com.petproject.motelservice.services.impl;



import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.petproject.motelservice.domain.payload.Email;
import com.petproject.motelservice.services.MailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	JavaMailSender emailSender;
	
	@Autowired
	SpringTemplateEngine templateEngine;

	@Override
	public void sendInvoiceEmail(Email email) throws Exception {
		 MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());
        helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process(email.getTemplate(), context);
        helper.setText(html, true);
        emailSender.send(message);
		System.out.println("Mail Send...");
	}
	

	

}
