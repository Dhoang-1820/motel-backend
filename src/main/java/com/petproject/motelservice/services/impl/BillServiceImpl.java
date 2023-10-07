package com.petproject.motelservice.services.impl;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.dto.RoomFeeEmail;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Bills;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.payload.Email;
import com.petproject.motelservice.domain.payload.request.SendInvoiceRequest;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.RoomBillEmail;
import com.petproject.motelservice.domain.query.response.RoomBillResponse;
import com.petproject.motelservice.domain.query.response.RoomFeeResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.BillRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.BillServices;
import com.petproject.motelservice.services.MailService;

@Service
public class BillServiceImpl implements BillServices {

	@Autowired
	BillRepository billRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	AccomodationsRepository accomodationsRepository;


	@Autowired
	MailService mailService;

	@Autowired
	ModelMapper mapper;

	@Override
	public List<BillDto> getMonthBillByAccomodation(Integer accomodationId, Date month) {
		Accomodations accomodations = accomodationsRepository.findById(accomodationId).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodations(accomodations);
		List<BillDto> result = new ArrayList<>();
		BillDto billDto = null;
		List<Bills> bills = null;
		for (Rooms room : rooms) {
			bills = billRepository.findBillMonthByRomm(month, room.getId());
			for (Bills bill : bills) {
				billDto = convertToDto(bill, room);
				result.add(billDto);
			}
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private BillDto convertToDto(Bills bill, Rooms room) {
		BillDto billDto = mapper.map(bill, BillDto.class);
		RoomResponse roomResponse = new RoomResponse(room.getId(), room.getName());
		billDto.setRoom(roomResponse);
		return billDto;
	}

	@Override
	public BillDto createOrUpdate(BillDto request) {
		Bills bill = null;
		Date createAt = null;
		if (request.getId() != null) {
			bill = billRepository.findById(request.getId()).orElse(null);
			createAt = bill.getCreatedAt();
		} else {
			bill = new Bills();
			createAt = new Date();
		}
		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
		Accomodations accomodations = room.getAccomodations();
		bill.setBillDate(request.getBillDate());
		bill.setCreatedAt(new Date());
//		bill.setFirstElectric(request.getFirstElectric());
//		bill.setLastElectric(request.getLastElectric());
//		bill.setElectricNum(request.getElectricNum());
//		bill.setFirstWater(request.getFirstWater());
//		bill.setLastWater(request.getLastWater());
//		bill.setWaterNum(request.getWaterNum());
		bill.setTotalPrice(computeBills(accomodations, room, request.getWaterNum(), request.getElectricNum()));
		bill.setIsPay(false);
		bill.setRoom(room);
		bill.setCreatedAt(createAt);
		bill = billRepository.save(bill);
		return convertToDto(bill, room);
	}

	private double computeBills(Accomodations accomodations, Rooms room, Integer waterNum, Integer electricNum) {
		double result = 0D;
//		List<RoomFees> fees = room.getFees();
//		for (RoomFees fee : fees) {
//			result += fee.getId().getFee().getPrice() * fee.getQuantity();
//		}
//		result += waterNum * accomodations.getWaterPrice();
//		result += electricNum * accomodations.getElectricPrice();
		return result;
	}

	@Override
	public List<InvoiceDto> getMonthInvoiceByAccomodation(Integer accomodationId, Date month) {
		InvoiceDto invoiceDto = new InvoiceDto();
		List<InvoiceDto> result = new ArrayList<>();
		List<RoomBillResponse> rooms = roomRepository.findRoomHasBill(accomodationId, month);
//		List<RoomFeeResponse> fees = roomFeeRepository.findRoomMonthBillByAccomodation(accomodationId, month);
		List<RoomFeeResponse> partialList = new ArrayList<>();
		for (RoomBillResponse room : rooms) {
			partialList = new ArrayList<>();
//			for (int i = 0; i < fees.size(); i++) {
//				if (room.getId().equals(fees.get(i).getRoomId())) {
//					partialList.add(fees.get(i));
//				}
//			}
			invoiceDto = new InvoiceDto();
			invoiceDto.setId(room.getId());
			invoiceDto.setBillId(room.getBillId());
			invoiceDto.setName(room.getName());
			invoiceDto.setIsSent(room.getIsSent());
			invoiceDto.setBillDate(room.getMonth());
			invoiceDto.setElectricNum(room.getElectricNum());
			invoiceDto.setWaterNum(room.getWaterNum());
			invoiceDto.setTotalPrice(room.getTotalPrice());
			invoiceDto.setIsPay(room.getIsPay());
			invoiceDto.setCreatedAt(room.getCreateAt());
			invoiceDto.setFees(partialList);
			result.add(invoiceDto);
		}
		return result;
	}

	@Override
	public void changePaymentStatus(Integer id) {
		Bills bills = billRepository.findById(id).orElse(null);
		bills.setIsPay(true);
		billRepository.save(bills);
		
	}
	
	private void sendInvoice(Integer billId, Integer roomId, Date month) {
//		List<RoomFeeResponse> fees = roomFeeRepository.findRoomMonthBill(roomId, month);
		RoomBillEmail roomBill = roomRepository.findRoomBillByMonth(roomId, month);
		Rooms room = roomRepository.findById(roomId).orElse(null);
		List<RoomFeeEmail> feeEmails = new ArrayList<>();
		Locale localeVN = new Locale("vi", "VN");
	    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
	    String price = null;
	    String total = null;
//		for (RoomFeeResponse fee : fees) {
//			price = currencyVN.format(fee.getPrice());
//			total = currencyVN.format(fee.getTotal());
//			feeEmails.add(new RoomFeeEmail(fee.getQuantity(), fee.getFeeId(), fee.getFeeName(), fee.getUnit(), price, total));
//		}
		 
		Email email = new Email();
		try {
			
	        
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			LocalDateTime now = LocalDateTime.now();
			Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
	        Map<String, Object> properties = new HashMap<>();
	        DateFormat monthFormat = new SimpleDateFormat("MM/yyyy");  
	        String monthInvoice = monthFormat.format((Date) roomBill.getMonth()); 	
	        email.setSubject("Hoá đơn tháng " + monthInvoice + " phòng " + room.getName());
	        
	        properties.put("toDate", dateFormat.format(Date.from(instant)));
	        properties.put("roomName", room.getName());
	        properties.put("invoiceMonth", monthInvoice);
	        properties.put("otherFees", feeEmails);
	        
	        price = currencyVN.format(roomBill.getElectricPrice());
	        properties.put("electricPrice", price);
	        
	        properties.put("electricNum", roomBill.getElectricNum());
	        properties.put("waterNum", roomBill.getWaterNum());
	        
	        price = currencyVN.format(roomBill.getWaterPrice());
	        properties.put("waterPrice", price);
	        
	        price = currencyVN.format(roomBill.getTotalWater());
	        properties.put("totalWater", price);
	        
	        price = currencyVN.format(roomBill.getTotalElectric());
	        properties.put("totalElectric", price);
	        
	        price = currencyVN.format(roomBill.getTotalPrice());
	        properties.put("totalPrice", price);
	        
	        email.setFrom("fromemail@gmail.com");
	        email.setTemplate("email.html");
	        email.setProperties(properties);
	        
	        String regex = "^(.+)@(.+)$";
	        Pattern pattern = Pattern.compile(regex);
	        
//	        List<Tenants> tenants = room.getTenants();
//			for (Tenants tenant : tenants) {
//				if (tenant.getIsStayed() == Boolean.TRUE) {
//					Matcher matcher = pattern.matcher(tenant.getEmail());
//					if (matcher.matches()) {
//						email.setTo(tenant.getEmail());
//						mailService.sendInvoiceEmail(email);
//					}
//				}
//			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendInvoice(SendInvoiceRequest request) {
		sendInvoice(request.getBillId(), request.getRoomId(), request.getMonth());
		Bills bill = billRepository.findById(request.getBillId()).orElse(null);
		bill.setIsSent(true);
		billRepository.save(bill);
	}

	

}
