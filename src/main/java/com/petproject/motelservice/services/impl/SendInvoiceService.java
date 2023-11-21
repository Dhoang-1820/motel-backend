package com.petproject.motelservice.services.impl;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.BankAccountDto;
import com.petproject.motelservice.domain.dto.BillServiceDto;
import com.petproject.motelservice.domain.dto.BillServiceEmail;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.BankAccountInfo;
import com.petproject.motelservice.domain.inventory.Bills;
import com.petproject.motelservice.domain.inventory.Contract;
import com.petproject.motelservice.domain.inventory.District;
import com.petproject.motelservice.domain.inventory.ElectricWaterNum;
import com.petproject.motelservice.domain.inventory.Province;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.ServicesBill;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.inventory.UserPreference;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.inventory.Ward;
import com.petproject.motelservice.domain.payload.Email;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.repository.BillRepository;
import com.petproject.motelservice.repository.ContractRepository;
import com.petproject.motelservice.repository.ElectricWaterNumRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.ServicesBillRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.services.MailService;

@Service
public class SendInvoiceService {
	
	@Autowired
	BillRepository billRepository;
	
	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	ElectricWaterNumRepository electricWaterNumRepository;
	
	@Autowired
	ServicesBillRepository servicesBillRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	public void sendRemind(UserPreference preference, Boolean isDetail) {
		Users user = preference.getUser();
		Date electricWaterDate = preference.getEletricWaterDate();
		DateFormat monthFormat = new SimpleDateFormat("dd/MM/yyyy");
		String electricWaterDateStr = monthFormat.format(electricWaterDate);
		Email email = new Email();
		Map<String, Object> properties = new HashMap<>();
		properties.put("electricWaterDate", electricWaterDateStr);
		properties.put("lanlord", user.getFirstname() + " " + user.getLastname());
		email.setSubject("[Nhắc nhở] Nhắc nhập chỉ số điện nước!");
		email.setFrom("fromemail@gmail.com");
		if (isDetail) {
			Date month = new Date();
			List<Accomodations> accomodations =  user.getAccomodations();
			List<Rooms> rooms = new ArrayList<>();
			for (Accomodations item : accomodations) {
				rooms.addAll(roomRepository.findRoomNoElectricWater(item.getId(), month));
			}
			List<String> roomsStr = new ArrayList<>();
			for (Rooms room : rooms) {
				roomsStr.add(room.getName());
			}
			properties.put("rooms", roomsStr);
			email.setTemplate("remind_detail.html");
		} else {
			email.setTemplate("remind.html");			
		}
		email.setProperties(properties);

		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(user.getEmail());

		if (matcher.matches()) {
			email.setTo(user.getEmail());
			try {
				mailService.sendInvoiceEmail(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private InvoiceDto getInvoiceDetail(Integer invoiceId, Date month, Boolean isReturn) {
		Bills bill = billRepository.findById(invoiceId).orElse(null);
		Rooms room = bill.getRoom();
		Contract contract = contractRepository.findByRoomIdAndIsActive(room.getId(), true);
		List<BillServiceDto> services = getContractServices(bill.getId(), room.getId(), month);
		return convert2Dto(bill, room, contract, services);
	}
	
	private List<BillServiceDto> getContractServices(Integer billId, Integer roomId, Date month) {
		List<BillServiceDto> services = new ArrayList<>();
		BillServiceDto dto = null;
		List<ServicesBill> servicesBill = servicesBillRepository.findByBillId(billId);
		ElectricWaterNum electricWaterNum = null;
		for (ServicesBill service : servicesBill) {
			dto = new BillServiceDto();
			dto.setServiceName(service.getServiceName());
			dto.setUnit(service.getUnit());
			dto.setPrice(service.getPrice());

			if (service.getServiceName().equals(Constants.ELECTRIC_PRICE_NAME)) {
				electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(roomId, month);
				if (electricWaterNum != null) {
					dto.setFirstElectricNum(electricWaterNum.getFirstElectric());
					dto.setLastElectricNum(electricWaterNum.getLastElectric());
					dto.setElectricNum(electricWaterNum.getElectricNum());
					dto.setQuantity(electricWaterNum.getElectricNum());
					dto.setTotalPrice(electricWaterNum.getElectricNum() * service.getPrice());
				} else {
					electricWaterNum = electricWaterNumRepository.findBeforeByRoomId(roomId, month);
					dto.setFirstElectricNum(electricWaterNum.getLastElectric());
					dto.setLastElectricNum(electricWaterNum.getLastElectric());
					dto.setElectricNum(0);
					dto.setQuantity(0);
					dto.setTotalPrice(0D);
				}
			} else if (service.getServiceName().equals(Constants.WATER_PRICE_NAME)) {
				electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(roomId, month);
				if (electricWaterNum != null) {
					dto.setFirstWaterNum(electricWaterNum.getFirstWater());
					dto.setLastWaterNum(electricWaterNum.getLastWater());
					dto.setWaterNum(electricWaterNum.getWaterNum());
					dto.setQuantity(electricWaterNum.getWaterNum());
					dto.setTotalPrice(electricWaterNum.getWaterNum() * service.getPrice());
				} else {
					electricWaterNum = electricWaterNumRepository.findBeforeByRoomId(roomId, month);
					dto.setFirstWaterNum(electricWaterNum.getLastWater());
					dto.setLastWaterNum(electricWaterNum.getLastWater());
					dto.setWaterNum(0);
					dto.setQuantity(0);
					dto.setTotalPrice(0D);
				}
			} else {
				dto.setQuantity(service.getQuantity());
				dto.setTotalPrice(service.getPrice() * service.getQuantity());
			}
			if (dto.getTotalPrice() != null) {
				services.add(dto);
			}
		}
		return services;
	}
	
	private Date getPreMonth(Date month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
	
	private InvoiceDto convert2Dto(Bills bill, Rooms room, Contract contract, List<BillServiceDto> services) {
		InvoiceDto dto = new InvoiceDto();
//		List<BillServiceDto> services = getServicePreview(contract, room, bill.getBillDate(), isReturn);
		dto.setBillDate(bill.getBillDate());
		dto.setCreatedAt(bill.getCreatedAt());
		dto.setId(bill.getId());
		dto.setIsPay(bill.getIsPay());
		dto.setIsSent(bill.getIsSent());
		dto.setDiscount(bill.getDiscount());
		Date preMonth = getPreMonth(bill.getBillDate());
		Double debt = billRepository.findDebtByRoom(room.getId(), preMonth);
		if (debt == null) {
			debt = 0D;
		}
		dto.setDebt(debt);
		dto.setPaidMoney(bill.getPaidMoney());
		dto.setPaymentDate(bill.getPaymentDate());
		dto.setRoom(new RoomResponse(room.getId(), room.getName(), room.getPrice(), room.getCapacity()));
		dto.setTotalPayment(bill.getTotalPayment());
		dto.setTotalPrice(bill.getTotalPrice());
		dto.setTotalService(bill.getTotalService());
		dto.setDescription(bill.getNote());
		dto.setNewDebt(bill.getTotalPayment() - bill.getPaidMoney());
		dto.setService(services);
		return dto;
	}

	public Boolean sendInvoice(Integer invoiceId, Date month) {
		Boolean result = false;
		try {
			Bills bill = billRepository.findById(invoiceId).orElse(null);
			InvoiceDto dto = getInvoiceDetail(invoiceId, month, false);
			Contract contract = contractRepository.findByRoomIdAndIsActive(dto.getRoom().getId(), true);
			Rooms room = contract.getRoom();
			Accomodations accomodation = room.getAccomodations();
			Users user = accomodation.getUser();
			List<BankAccountInfo> banks = user.getBankAccountInfos();
			List<Tenants> tenants = contract.getTenants();
			Tenants tenant = tenantRepository.findById(contract.getRepresentative()).orElse(null);
			Locale localeVN = new Locale("vi", "VN");
			NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
			List<BillServiceDto> services = dto.getService();
			BillServiceEmail serviceEmail = null;
			List<BillServiceEmail> serviceEmails = new ArrayList<>();
			for (BillServiceDto service : services) {
				serviceEmail = new BillServiceEmail();
				serviceEmail.setElectricNum(service.getElectricNum());
				serviceEmail.setFirstElectricNum(service.getFirstElectricNum());
				serviceEmail.setLastElectricNum(service.getLastElectricNum());
				serviceEmail.setWaterNum(service.getWaterNum());
				serviceEmail.setFirstWaterNum(service.getFirstWaterNum());
				serviceEmail.setLastWaterNum(service.getLastWaterNum());
				serviceEmail.setServiceName(service.getServiceName());
				serviceEmail.setQuantity(service.getQuantity());
				serviceEmail.setUnit(service.getUnit());
				serviceEmail.setPrice(currencyVN.format(service.getPrice()));
				serviceEmail.setTotalPrice(currencyVN.format(service.getTotalPrice()));
				serviceEmails.add(serviceEmail);
			}

			List<BankAccountDto> bankAccounts = new ArrayList<>();
			BankAccountDto bankAccountDto = null;
			for (BankAccountInfo bank : banks) {
				bankAccountDto = mapper.map(bank, BankAccountDto.class);
				bankAccounts.add(bankAccountDto);
			}

			String totalService = null;
			String debt = null;
			String discount = null;
			String totalPrice = null;
			String totalPayment = null;
			totalService = currencyVN.format(dto.getTotalService());
			debt = currencyVN.format(dto.getDebt());
			discount = currencyVN.format(dto.getDiscount());
			totalPrice = currencyVN.format(dto.getTotalPrice());
			totalPayment = currencyVN.format(dto.getTotalPayment());

			Email email = new Email();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			LocalDateTime now = LocalDateTime.now();
			Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
			Map<String, Object> properties = new HashMap<>();
			DateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
			String monthInvoice = monthFormat.format((Date) dto.getBillDate());
			email.setSubject("Hoá đơn tháng " + monthInvoice + " phòng " + dto.getRoom().getName());

			properties.put("toDate", dateFormat.format(Date.from(instant)));
			properties.put("roomName", dto.getRoom().getName());
			properties.put("invoiceMonth", monthInvoice);
			properties.put("services", serviceEmails);
			properties.put("personNum", tenants.size());
			properties.put("startDate", dateFormat.format(contract.getStartDate()));
			properties.put("representative", tenant.getFirstName() + " " + tenant.getLastName());
			properties.put("representativeEmail", tenant.getEmail());
			properties.put("totalService", totalService);
			properties.put("debt", debt);
			properties.put("discount", discount);
			properties.put("totalPrice", totalPrice);
			properties.put("totalPayment", totalPayment);
			properties.put("banks", bankAccounts);
			properties.put("accomodationName", accomodation.getName());
			Address address = accomodation.getAddress();
			Ward ward = address.getWard();
			District district = ward.getDistrict();
			Province province = district.getProvince();
			properties.put("accomodationAddress", address.getAddressLine() + " " + ward.getWard() + " "
					+ district.getDistrict() + " " + province.getProvince());

			email.setFrom("fromemail@gmail.com");
			email.setTemplate("email_test.html");
			email.setProperties(properties);

			String regex = "^(.+)@(.+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(tenant.getEmail());

			if (matcher.matches()) {
				email.setTo(tenant.getEmail());
				mailService.sendInvoiceEmail(email);
			}

			Integer quantitySent = bill.getQuantitySent() + 1;
			bill.setQuantitySent(quantitySent);
			billRepository.save(bill);	
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
