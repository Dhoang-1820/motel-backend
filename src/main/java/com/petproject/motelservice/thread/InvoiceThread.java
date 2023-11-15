package com.petproject.motelservice.thread;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.petproject.motelservice.services.impl.SendInvoiceService;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvoiceThread implements Runnable{
	
	@Autowired
	SendInvoiceService sendInvoiceService;
	
	private Integer invoiceId;
	private Date month;
	

	public InvoiceThread() {
		System.out.println("init thread");
	}

	public InvoiceThread(Integer invoiceId, Date month) {
		this.invoiceId = invoiceId;
		this.month = month;
	}

	@Override
	public synchronized void run() {
		sendInvoiceService.sendInvoice(invoiceId, month);
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}
	
	

}
