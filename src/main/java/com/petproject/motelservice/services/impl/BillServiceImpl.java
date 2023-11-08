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
import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.BillServiceDto;
import com.petproject.motelservice.domain.dto.BillServiceEmail;
import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.inventory.AccomodationUtilities;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.BankAccountInfo;
import com.petproject.motelservice.domain.inventory.Bills;
import com.petproject.motelservice.domain.inventory.Contract;
import com.petproject.motelservice.domain.inventory.ContractService;
import com.petproject.motelservice.domain.inventory.District;
import com.petproject.motelservice.domain.inventory.ElectricWaterNum;
import com.petproject.motelservice.domain.inventory.InvoiceType;
import com.petproject.motelservice.domain.inventory.Province;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.inventory.Ward;
import com.petproject.motelservice.domain.payload.Email;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.InvoiceResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.BillRepository;
import com.petproject.motelservice.repository.ContractRepository;
import com.petproject.motelservice.repository.ElectricWaterNumRepository;
import com.petproject.motelservice.repository.InvoiceTypeRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.TenantRepository;
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
	ElectricWaterNumRepository electricWaterNumRepository;
	
	@Autowired
	ContractRepository contractRepository;

	@Autowired
	MailService mailService;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired 
	InvoiceTypeRepository invoiceTypeRepository;

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
//			bills = billRepository.findBillMonthByRomm(month, room.getId());
			for (Bills bill : bills) {
				billDto = convertToDto(bill, room);
				result.add(billDto);
			}
		}
		return result;
	}

	private BillDto convertToDto(Bills bill, Rooms room) {
		BillDto billDto = mapper.map(bill, BillDto.class);
		RoomResponse roomResponse = new RoomResponse(room.getId(), room.getName(), room.getPrice());
		billDto.setRoom(roomResponse);
		return billDto;
	}

	private Date getPreMonth(Date month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	private Double computeBills(Contract contract, Rooms room, Date month, List<BillServiceDto> services) {
		Double result = 0D;
		for (BillServiceDto service : services) {
			result += service.getTotalPrice();
		}
		return result;
	}
	
	@Override
	public List<InvoiceResponse> getInvoice(Integer accomodationId, Date month) {
		Date currentMonth = new Date();
		Calendar current = Calendar.getInstance();
		current.setTime(currentMonth);
		Calendar request = Calendar.getInstance();
		request.setTime(month);
		List<InvoiceResponse> invoices = null;
		if (request.get(Calendar.MONTH) >= current.get(Calendar.MONTH) && request.get(Calendar.YEAR) >= current.get(Calendar.YEAR)) {
			invoices = billRepository.findCurrentInvoiceByMonth(accomodationId, month);			
		} else {
			invoices = billRepository.findInvoiceByMonth(accomodationId, month);		
		}
		return invoices;
	}

	@Override
	public List<InvoiceDto> getInvoiceByMonth(Integer accomodationId, Date month, Boolean isReturn) {
		InvoiceDto invoiceDto = null;
		List<InvoiceResponse> invoices = null;
		Date currentMonth = new Date();
		Calendar current = Calendar.getInstance();
		current.setTime(currentMonth);
		Calendar request = Calendar.getInstance();
		request.setTime(month);
		if (!isReturn) {
			if (request.get(Calendar.MONTH) >= current.get(Calendar.MONTH) && request.get(Calendar.YEAR) >= current.get(Calendar.YEAR)) {
				invoices = billRepository.findCurrentInvoiceByMonth(accomodationId, month);			
			} else {
				invoices = billRepository.findInvoiceByMonth(accomodationId, month);		
			}
		} else {
			invoices = billRepository.findCurrentReturnInvoiceByMonth(accomodationId, month);
		}
		
		List<InvoiceDto> result = new ArrayList<>();
		for (InvoiceResponse invoice : invoices) {
			invoiceDto = new InvoiceDto();
			invoiceDto.setId(invoice.getBillId());
			invoiceDto.setBillDate(invoice.getBillDate());
			invoiceDto.setRoom(new RoomResponse(invoice.getRoomId(), invoice.getRoomName(), null));
			invoiceDto.setIsPay(invoice.getIsPay());
			invoiceDto.setDebt(invoice.getDebt());
			invoiceDto.setQuantitySent(invoice.getQuantitySent());
			invoiceDto.setRepresentative(invoice.getRepresentative());
			invoiceDto.setDiscount(invoice.getDiscount());
			invoiceDto.setPaidMoney(invoice.getPaidMoney());
			invoiceDto.setTotalPrice(invoice.getTotalPrice());
			invoiceDto.setPaymentDate(invoice.getPaymentDate());
			invoiceDto.setTotalPayment(invoice.getTotalPayment());
			invoiceDto.setReturnDate(invoice.getReturnDate());
			result.add(invoiceDto);
		}
		return result;
	}
	
	private List<BillServiceDto> getBillRoomService(Contract contract, Rooms room, Date month) {
		BillServiceDto dto = new BillServiceDto();
		List<BillServiceDto> services = new ArrayList<>();
		AccomodationUtilities service = null;
		List<ContractService> contractServices = contract.getContractServices();
		ElectricWaterNum electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(room.getId(), month);
		for (ContractService item :contractServices) {
			dto = new BillServiceDto();
			service = item.getId().getAccomodationService();
			dto.setServiceName(service.getName());
			dto.setUnit(service.getUnit());
			dto.setPrice(service.getPrice());
			
			if (service.getName().equals(Constants.ELECTRIC_PRICE_NAME)) {
				if (electricWaterNum != null) {
					dto.setFirstElectricNum(electricWaterNum.getFirstElectric());
					dto.setLastElectricNum(electricWaterNum.getLastElectric());
					dto.setElectricNum(electricWaterNum.getElectricNum());
					dto.setTotalPrice(electricWaterNum.getElectricNum() * service.getPrice());
				} 
//				else {
//					electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(room.getId(), preMonth);
//					dto.setFirstElectricNum(electricWaterNum.getLastElectric());
//					dto.setLastElectricNum(0);
//					dto.setElectricNum(0);
//					dto.setTotalPrice(0D);
//				}
			} else if (service.getName().equals(Constants.WATER_PRICE_NAME)) {
				if (electricWaterNum != null) {
					dto.setFirstWaterNum(electricWaterNum.getFirstWater());
					dto.setLastWaterNum(electricWaterNum.getLastWater());
					dto.setWaterNum(electricWaterNum.getWaterNum());
					dto.setTotalPrice(electricWaterNum.getWaterNum() * service.getPrice());
				} 
//				else {
//					electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(room.getId(), preMonth);
//					dto.setFirstWaterNum(electricWaterNum.getLastWater());
//					dto.setLastWaterNum(0);
//					dto.setWaterNum(0);
//					dto.setTotalPrice(0D);
//				}
			} else {
				dto.setQuantity(item.getQuantity());
				dto.setTotalPrice(service.getPrice() * item.getQuantity());
			}
			if (dto.getTotalPrice() != null) {
				services.add(dto);
			}
		}
		return services;
	}
	
	private List<BillServiceDto> getBillServiceByBillId(Contract contract, Rooms room, Date month) {
		List<BillServiceDto> services = new ArrayList<>();
		BillServiceDto dto = new BillServiceDto();
		Date preMonth = getPreMonth(month);
		// add room monthly money
		dto.setServiceName(Constants.ROOM_PRICE_NAME);
		dto.setUnit(Constants.ROOM_UNIT);
		dto.setPrice(room.getPrice()); 
		dto.setQuantity(1);
		dto.setTotalPrice(room.getPrice());
		services.add(dto);
		services.addAll(getBillRoomService(contract, room, preMonth));
		return services;
	}

	@Override
	public InvoiceDto getInvoiceDetail(Integer invoiceId, Boolean isReturn) {
		Bills bill = billRepository.findById(invoiceId).orElse(null);
		Rooms room = bill.getRoom();
		Contract contract = contractRepository.findByRoomIdAndIsActive(room.getId(), true);
		return convert2Dto(bill, room, contract, isReturn);
	}
	
	private InvoiceDto convert2Dto(Bills bill, Rooms room, Contract contract, Boolean isReturn) {
		InvoiceDto dto = new InvoiceDto();
		List<BillServiceDto> services = null;
		if (!isReturn) {
			services = getBillServiceByBillId(contract, room, bill.getBillDate());			
		} else {
			services = getBillRoomService(contract, room, bill.getBillDate());		
		}
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
		dto.setRoom(new RoomResponse(room.getId(), room.getName(), room.getPrice()));
		dto.setTotalPayment(bill.getTotalPayment());
		dto.setTotalPrice(bill.getTotalPrice());
		dto.setTotalService(bill.getTotalService());
		dto.setDescription(bill.getNote());
		dto.setNewDebt(bill.getTotalPayment() - bill.getPaidMoney());
		dto.setService(services);
		return dto;
	}
	
	private Bills convertBill(Bills bill, Rooms room, Date month, Contract contract, Double totalPrice) {
		Date preMonth = getPreMonth(month);
		Double debt = billRepository.findDebtByRoom(room.getId(), preMonth);
		Double discount = 0D;
		bill.setTotalService(totalPrice);
		bill.setDiscount(discount);
		bill.setIsPay(Boolean.FALSE);
		bill.setIsSent(Boolean.FALSE);
		if (debt == null) {
			debt = 0D;
		} 
		bill.setDebt(debt);	
		totalPrice += debt;
		bill.setTotalPrice(totalPrice);
		totalPrice -= discount;
		bill.setTotalPayment(totalPrice); 
		bill.setPaidMoney(totalPrice);
		bill.setQuantitySent(0);
		bill.setBillDate(month);
		bill.setRoom(room);
		
		bill.setCreatedAt(new Date());
		return bill;
	}
	
	@Override
	public List<InvoiceDto> issueInvoiceByRoomId(Integer roomId,  Date month) {
		List<InvoiceDto> result = null;
		InvoiceDto invoice = getIssueInvoicePreview(roomId, month);
		result = issueInvoice(invoice, false);
		return result;
	}
	
	@Override
	public List<InvoiceDto> issueInvoice(InvoiceDto request, Boolean isReturn) {
		List<InvoiceDto> result = null;
		try {
			Bills bill = new Bills();
			Contract contract = contractRepository.findByRoomIdAndIsActive(request.getRoom().getId(), true);
			Rooms room = contract.getRoom();
			List<BillServiceDto> services = new ArrayList<>();
			if (!isReturn) {
				services = getBillServiceByBillId(contract, room, request.getBillDate());				
			} else {
				services = getBillRoomService(contract, room, request.getBillDate());
			}
			Double totalPrice = computeBills(contract, room, request.getBillDate(), services);
			bill = convertBill(bill, room, request.getBillDate(), contract, totalPrice);
			Double discount = request.getDiscount();
			Double debt = 0D;
			if (discount == null) {
				discount = 0D;
			}
			bill.setTotalService(totalPrice);
			bill.setDiscount(discount);
			bill.setIsPay(Boolean.FALSE);
			bill.setIsSent(Boolean.FALSE);
			bill.setTotalPrice(totalPrice);
			totalPrice -= discount;
			bill.setTotalPayment(totalPrice); 
			bill.setPaidMoney(request.getPaidMoney());
			debt = request.getTotalPayment() - request.getPaidMoney();
			bill.setDebt(debt);	
			bill.setQuantitySent(0);
			bill.setBillDate(request.getBillDate());
			bill.setRoom(room);
			InvoiceType type = null;
			if (!isReturn) {
				type = invoiceTypeRepository.findById(1).orElse(null);	
				result = getInvoiceByMonth(room.getAccomodations().getId(), request.getBillDate(), false);
			} else {
				type = invoiceTypeRepository.findById(2).orElse(null);				
			}
			bill.setInvoiceType(type);
			bill = billRepository.save(bill);
			sendInvoice(bill.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public InvoiceDto updateInvoice(InvoiceDto request) {
		InvoiceDto result = null;
		try {
			Bills bill = billRepository.findById(request.getId()).orElse(null);
			bill.setDiscount(request.getDiscount());
			bill.setTotalPayment(request.getTotalPayment());
			bill.setDebt(request.getNewDebt());
			bill.setPaidMoney(request.getPaidMoney());
			bill.setTotalPrice(request.getDebt() + request.getTotalService());
			bill.setNote(request.getDescription());
			if (request.getIsPay() != null && request.getIsPay()) {
				bill.setIsPay(request.getIsPay());
				bill.setPaymentDate(new Date());
			}
			bill = billRepository.save(bill);
			Rooms room = bill.getRoom();
			Contract contract = contractRepository.findByRoomIdAndIsActive(room.getId(), true);
			result = convert2Dto(bill, room, contract, request.getIsReturnBill());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public InvoiceDto getIssueInvoicePreview(Integer roomId, Date month) {
		Contract contract = contractRepository.findByRoomIdAndIsActive(roomId, true);
		Rooms room = contract.getRoom();
		Bills bill = new Bills();
		List<BillServiceDto> services = getBillServiceByBillId(contract, room, month);
		Double totalPrice = computeBills(contract, room, month, services);
		bill = convertBill(bill, room, month, contract, totalPrice);
		return convert2Dto(bill, room, contract, false);
	}
	
	@Override
	public Boolean changePaymentStatus(Integer invoiceId) {
		Boolean result = false;
		try {
			Bills bill = billRepository.findById(invoiceId).orElse(null);
			bill.setIsPay(true);
			bill.setPaymentDate(new Date());
			billRepository.save(bill);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public InvoiceDto getReturnRoomPreview(ReturnRoomRequest request) {
		InvoiceDto dto = new InvoiceDto();
		Contract contract = contractRepository.findByRoomIdAndIsActive(request.getRoomId(), true); 
		Rooms room = contract.getRoom();
		List<BillServiceDto> services = getBillRoomService(contract, room, request.getReturnDate());
		Double totalPrice = computeBills(contract, room, request.getReturnDate(), services);
		Bills bill = new Bills();
		bill = convertBill(bill, room, request.getReturnDate(), contract, totalPrice);
		
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
		dto.setRoom(new RoomResponse(room.getId(), room.getName(), room.getPrice()));
		dto.setTotalPayment(bill.getTotalPayment());
		dto.setTotalPrice(bill.getTotalPrice());
		dto.setTotalService(bill.getTotalService());
		dto.setDescription(bill.getNote());
		dto.setNewDebt(bill.getTotalPayment() - bill.getPaidMoney());
		dto.setService(services);
		return dto;
	}

	@Override
	public Boolean sendInvoice(Integer invoiceId) {
		Boolean result = false;
		try {
			Bills bill = billRepository.findById(invoiceId).orElse(null);
			InvoiceDto dto = getInvoiceDetail(invoiceId, false);
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
			List<BillServiceEmail> serviceEmails =  new ArrayList<>();
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
			
		    String totalService =null;
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
	        properties.put("accomodationAddress", address.getAddressLine() + " " + ward.getWard() + " " + district.getDistrict() + " " + province.getProvince());
	       
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
	
	@Override
	public List<ElectricityWaterDto> getElectricWaterNumByAccomodation(Integer accomodationId, Date month) {
		List<ElectricityWaterDto> result = new ArrayList<>();
		List<ElectricWaterNum> num = electricWaterNumRepository.findByAccomodationAndMonth(accomodationId, month);
		for (ElectricWaterNum item : num) {
			result.add(convert2ElectricityWaterDto(item));
		}
		
		return result;
	}
	
	@Override
	public Boolean checkIsRoomInputElectricWater(Integer roomId, Date month) {
		Boolean result = false;
		ElectricWaterNum num = electricWaterNumRepository.findByRoomIdAndMonth(roomId, month);
		if (num != null) {
			result = true;
		}
		return result;
	}

	@Override
	public ElectricityWaterDto saveElectricWaterNum(ElectricityWaterDto request) {
		ElectricWaterNum num = new ElectricWaterNum();
		if (request.getId() != null) {
			num = electricWaterNumRepository.findById(request.getId()).orElse(null);
		}
		num.setFirstElectric(request.getFirstElectric());
		num.setLastElectric(request.getLastElectric());
		num.setElectricNum(request.getElectricNum());
		num.setFirstWater(request.getFirstWater());
		num.setLastWater(request.getLastWater());
		num.setWaterNum(request.getWaterNum());
		num.setMonth(request.getMonth());
		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
		num.setRoom(room);
		num = electricWaterNumRepository.save(num);
		return convert2ElectricityWaterDto(num);
	}

	private ElectricityWaterDto convert2ElectricityWaterDto(ElectricWaterNum num) {
		ElectricityWaterDto result = new ElectricityWaterDto();
		result.setId(num.getId());
		result.setFirstElectric(num.getFirstElectric());
		result.setLastElectric(num.getLastElectric());
		result.setElectricNum(num.getElectricNum());
		result.setFirstWater(num.getFirstWater());
		result.setLastWater(num.getLastWater());
		result.setWaterNum(num.getWaterNum());
		result.setMonth(num.getMonth());
		Rooms room = num.getRoom();
		result.setRoom(new RoomResponse(room.getId(), room.getName(), room.getPrice()));
		return result;
	}

	@Override
	public Boolean removeInvoice(Integer invoiceId) {
		Boolean result = false;
		try {
			Bills bill = billRepository.findById(invoiceId).orElse(null);
			billRepository.delete(bill);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<InvoiceDto> returnRoom(InvoiceDto request) {
		List<InvoiceDto> result = null;
		issueInvoice(request, true);
		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
		Contract contract = contractRepository.findByRoomIdAndIsActive(request.getRoom().getId(), true);
		List<Tenants> tenants = contract.getTenants();
		result = getInvoiceByMonth(room.getAccomodations().getId(), request.getBillDate(), true);
		room.setIsRent(false);
		roomRepository.save(room);
		for (Tenants tenant : tenants) {
			tenant.setIsStayed(false);
			tenant.setEndDate(request.getReturnDate());
			tenantRepository.save(tenant);
		}
		return result;
	}
	
	@Override
	public Map<String, Boolean> checkIsReturnValid(Integer roomId, Date month) {
		Map<String, Boolean> result = new HashMap<>();
		Bills bill = billRepository.findByMonthAndRoom(month, roomId);
		if (bill != null) {
			result.put("bill", true);
		}
		Boolean isInputed = checkIsRoomInputElectricWater(roomId, month);
		if (!isInputed) {
			result.put("inputed", true);
		}
		
		return result;
	}

}
