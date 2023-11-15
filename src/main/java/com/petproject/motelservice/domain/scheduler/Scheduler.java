package com.petproject.motelservice.domain.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.UserPreference;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.query.response.InvoiceResponse;
import com.petproject.motelservice.repository.UserPreferenceRepository;
import com.petproject.motelservice.services.BillServices;

@Component
public class Scheduler {
	
	@Autowired
	UserPreferenceRepository preferenceRepository;
	
	@Autowired
	BillServices billServices;

	@Scheduled(cron = "0 20 1 * * *")
	public void cronJobSch() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println("Java cron job expression:: " + strDate);
		List<UserPreference> preferences = preferenceRepository.findIssueDateByDate(now);
		Users user = null;
		List<InvoiceResponse> invoices = null;
		List<Accomodations> accomodations = null;
		for (UserPreference preference : preferences) {
			user = preference.getUser();
			accomodations = user.getAccomodations();
			for (Accomodations accomodation : accomodations) {
				invoices = billServices.getInvoice(accomodation.getId(), now);
				for (InvoiceResponse invoice : invoices) {
					billServices.issueInvoiceByRoomId(invoice.getRoomId(), now);
				}
			}
		}
//		preferences = preferenceRepository.findRemindByDate(now);
		
	}
}
