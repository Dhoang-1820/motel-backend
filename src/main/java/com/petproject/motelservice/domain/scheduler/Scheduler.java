package com.petproject.motelservice.domain.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.petproject.motelservice.services.impl.SendInvoiceService;

@Component
public class Scheduler {
	
	@Autowired
	UserPreferenceRepository preferenceRepository;
	
	@Autowired
	BillServices billServices;
	
	@Autowired
	SendInvoiceService sendInvoiceService;

	@Scheduled(cron = "0 01 00 * * *")
	public void cronJobSch() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		int dayNow = cal.get(Calendar.DAY_OF_MONTH);
		int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		String strDate = sdf.format(now);
		System.out.println("Java cron job expression: " + strDate);
		List<UserPreference> preferences = preferenceRepository.findIssueDateByDate(cal.getTime());
		if (dayNow == lastDayOfMonth) {
			for (int i = dayNow; i <= 31; i++) {
				cal.set(Calendar.DATE, i);
				preferences.addAll(preferenceRepository.findIssueDateByDate(cal.getTime()));
			}
		}
		Users user = null;
		List<InvoiceResponse> invoices = null;
		List<Accomodations> accomodations = null;
		for (UserPreference preference : preferences) {
			user = preference.getUser();
			accomodations = user.getAccomodations();
			for (Accomodations accomodation : accomodations) {
				invoices = billServices.getInvoice(accomodation.getId(), cal.getTime());
				for (InvoiceResponse invoice : invoices) {
					billServices.issueInvoiceByRoomId(invoice.getRoomId(), cal.getTime());
				}
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, 3);
		int next3Day = calendar.get(Calendar.DAY_OF_MONTH);
		if (next3Day < lastDayOfMonth) {
			preferences = preferenceRepository.findRemindByDate(calendar.getTime());
			
			for (UserPreference preference : preferences) {
				sendInvoiceService.sendRemind(preference, false);
			}
		}
		
		preferences = preferenceRepository.findRemindByDate(now);
		
		for (UserPreference preference : preferences) {
			sendInvoiceService.sendRemind(preference, true);
		}
		
	}
}
