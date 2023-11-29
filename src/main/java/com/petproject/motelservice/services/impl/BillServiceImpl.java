package com.petproject.motelservice.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.BillServiceDto;
import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.inventory.AccomodationUtilities;
import com.petproject.motelservice.domain.inventory.Bills;
import com.petproject.motelservice.domain.inventory.Contract;
import com.petproject.motelservice.domain.inventory.ContractService;
import com.petproject.motelservice.domain.inventory.ElectricWaterNum;
import com.petproject.motelservice.domain.inventory.InvoiceType;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.ServicesBill;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.request.ConfirmInvoiceRequest;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.InvoiceResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.BillRepository;
import com.petproject.motelservice.repository.ContractRepository;
import com.petproject.motelservice.repository.ElectricWaterNumRepository;
import com.petproject.motelservice.repository.InvoiceTypeRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.ServicesBillRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.services.BillServices;
import com.petproject.motelservice.services.MailService;
import com.petproject.motelservice.thread.InvoiceThread;

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
	ServicesBillRepository servicesBillRepository;

	@Autowired
	ModelMapper mapper;
	
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired 
	InvoiceThread invoiceThread;

	private Date getPreMonth(Date month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	private Date getNextMonth(Date month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, 1);
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
	public ElectricityWaterDto getElectricWaterNumByMonthAndRoom(Integer roomId, Date month) {
		ElectricityWaterDto result = null;
		ElectricWaterNum electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(roomId, month);
		if (electricWaterNum != null) {
			result = convert2ElectricityWaterDto(electricWaterNum);
		}
		return result;
	}
	

	@Override
	public Boolean checkIsCanRemoveEletricWater(Integer roomId, Date month) {
		Date nextMonth = getNextMonth(month);
		Bills bill = billRepository.findByMonthAndRoom(nextMonth, roomId);
		return bill == null;
	}
	

	@Override
	public Boolean removeEletricWater(Integer id) {
		Boolean result = false;
		ElectricWaterNum electricWaterNum = electricWaterNumRepository.findById(id).orElse(null);
		try {
			electricWaterNumRepository.delete(electricWaterNum);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<InvoiceResponse> getInvoice(Integer accomodationId, Date month) {
		List<InvoiceResponse> invoices = null;
		invoices = billRepository.findCurrentInvoiceByMonth(accomodationId, month);
		return invoices;
	}

	@Override
	public List<InvoiceDto> getInvoiceByMonth(Integer accomodationId, Date month, Boolean isReturn) {
		InvoiceDto invoiceDto = null;
		List<InvoiceResponse> invoices = null;
		if (!isReturn) {
			invoices = billRepository.findCurrentInvoiceByMonth(accomodationId, month);
		} else {
			invoices = billRepository.findCurrentReturnInvoiceByMonth(accomodationId, month);
		}

		List<InvoiceDto> result = new ArrayList<>();
		for (InvoiceResponse invoice : invoices) {
			invoiceDto = new InvoiceDto();
			invoiceDto.setId(invoice.getBillId());
			invoiceDto.setBillDate(invoice.getBillDate());
			invoiceDto.setRoom(new RoomResponse(invoice.getRoomId(), invoice.getRoomName(), null, null));
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

	private List<BillServiceDto> getRoomServiceAndCompute(Contract contract, Rooms room, Date month) {
		BillServiceDto dto = new BillServiceDto();
		List<BillServiceDto> services = new ArrayList<>();
		AccomodationUtilities service = null;
		List<ContractService> contractServices = contract.getContractServices();
		ElectricWaterNum electricWaterNum = null;
		for (ContractService item : contractServices) {
			dto = new BillServiceDto();
			service = item.getId().getAccomodationService();
			dto.setServiceName(service.getName());
			dto.setUnit(service.getUnit());
			dto.setPrice(service.getPrice());

			if (service.getName().equals(Constants.ELECTRIC_PRICE_NAME)) {
				electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(room.getId(), month);
				if (electricWaterNum != null) {
					dto.setFirstElectricNum(electricWaterNum.getFirstElectric());
					dto.setLastElectricNum(electricWaterNum.getLastElectric());
					dto.setElectricNum(electricWaterNum.getElectricNum());
					dto.setQuantity(electricWaterNum.getElectricNum());
					dto.setTotalPrice(electricWaterNum.getElectricNum() * service.getPrice());
				} else {
					electricWaterNum = electricWaterNumRepository.findBeforeByRoomId(room.getId(), month);
					if (electricWaterNum != null) {
						dto.setFirstElectricNum(electricWaterNum.getLastElectric());
						dto.setLastElectricNum(electricWaterNum.getLastElectric());
					} else {
						dto.setFirstElectricNum(0);
						dto.setLastElectricNum(0);
					}
					dto.setElectricNum(0);
					dto.setQuantity(0);
					dto.setTotalPrice(0D);
					
				}
			} else if (service.getName().equals(Constants.WATER_PRICE_NAME)) {
				electricWaterNum = electricWaterNumRepository.findByRoomIdAndMonth(room.getId(), month);
				if (electricWaterNum != null) {
					dto.setFirstWaterNum(electricWaterNum.getFirstWater());
					dto.setLastWaterNum(electricWaterNum.getLastWater());
					dto.setWaterNum(electricWaterNum.getWaterNum());
					dto.setQuantity(electricWaterNum.getWaterNum());
					dto.setTotalPrice(electricWaterNum.getWaterNum() * service.getPrice());
				} else {
					electricWaterNum = electricWaterNumRepository.findBeforeByRoomId(room.getId(), month);
					if (electricWaterNum != null) {
						dto.setFirstWaterNum(electricWaterNum.getLastWater());
						dto.setLastWaterNum(electricWaterNum.getLastWater());
					} else {
						dto.setFirstWaterNum(0);
						dto.setLastWaterNum(0);
					}
					dto.setWaterNum(0);
					dto.setQuantity(0);
					dto.setTotalPrice(0D);
				}
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

	@Override
	public InvoiceDto getInvoiceDetail(Integer invoiceId, Date month, Boolean isReturn) {
		Bills bill = billRepository.findById(invoiceId).orElse(null);
		Rooms room = bill.getRoom();
		Contract contract = contractRepository.findByRoomIdAndIsActive(room.getId(), true);
		List<BillServiceDto> services = getContractServices(bill.getId(), room.getId(), month);
		return convert2Dto(bill, room, contract, services);
	}

	private List<BillServiceDto> getRoomService(Contract contract, Rooms room, Date month, Boolean isReturn) {
		List<BillServiceDto> services = new ArrayList<>();
		// check if it is a return bill or not.
		if (!isReturn) {
			Date nextMonth = getNextMonth(month);
			DateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
			String monthInvoice = monthFormat.format(nextMonth);
			BillServiceDto dto = new BillServiceDto();
			Date preMonth = getPreMonth(month);
			// add room monthly money
			dto.setServiceName(Constants.ROOM_PRICE_NAME + " tháng " + monthInvoice);
			dto.setUnit(Constants.ROOM_UNIT);
			dto.setPrice(room.getPrice());
			dto.setQuantity(1);
			dto.setTotalPrice(room.getPrice());
			services.add(dto);
			services.addAll(getRoomServiceAndCompute(contract, room, preMonth));
		} else {
			services = getRoomServiceAndCompute(contract, room, month);
		}
		return services;
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
					if (electricWaterNum != null) {
						dto.setFirstElectricNum(electricWaterNum.getLastElectric());
						dto.setLastElectricNum(electricWaterNum.getLastElectric());
					} else {
						dto.setFirstElectricNum(0);
						dto.setLastElectricNum(0);
					}
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
					if (electricWaterNum != null) {
						dto.setFirstWaterNum(electricWaterNum.getLastWater());
						dto.setLastWaterNum(electricWaterNum.getLastWater());
					} else {
						dto.setFirstWaterNum(0);
						dto.setLastWaterNum(0);
					}
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

	private InvoiceDto convert2Dto(Bills bill, Rooms room, Contract contract, List<BillServiceDto> services) {
		InvoiceDto dto = new InvoiceDto();
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

	private Bills generateBill(Bills bill, Rooms room, Date month, Contract contract, Double totalPrice) {
		bill = new Bills();
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
	public List<InvoiceDto> issueInvoiceByRoomId(Integer roomId, Date month) {
		List<InvoiceDto> result = null;
		InvoiceDto invoice = getIssueInvoicePreview(roomId, month);
		result = issueInvoice(invoice, false);
		return result;
	}

	@Override
	public List<InvoiceDto> issueInvoice(InvoiceDto request, Boolean isReturn) {
		List<InvoiceDto> result = null;
		try {
			Bills bill = null;
			Contract contract = contractRepository.findByRoomIdAndIsActive(request.getRoom().getId(), true);
			Rooms room = contract.getRoom();
			List<BillServiceDto> services = getRoomService(contract, room, request.getBillDate(), isReturn);

			Double totalPrice = computeBills(contract, room, request.getBillDate(), services);
			bill = generateBill(bill, room, request.getBillDate(), contract, totalPrice);
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
			bill.setQuantitySent(1);
			bill.setBillDate(request.getBillDate());
			bill.setIsActive(Boolean.TRUE);
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

			saveInvoiceService(services, bill, room);
			sendInvoiceEmail(bill.getId(), bill.getBillDate());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void saveInvoiceService(List<BillServiceDto> services, Bills bill, Rooms room) {
		ServicesBill servicesBill = null;
		for (BillServiceDto item : services) {
			servicesBill = new ServicesBill();
			servicesBill.setPrice(item.getTotalPrice());
			servicesBill.setUnit(item.getUnit());
			servicesBill.setQuantity(item.getQuantity());
			servicesBill.setServiceName(item.getServiceName());
			servicesBill.setBill(bill);
			servicesBillRepository.save(servicesBill);
		}
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
			List<BillServiceDto> services = getRoomService(contract, room, bill.getBillDate(),
					request.getIsReturnBill());
			result = convert2Dto(bill, room, contract, services);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public InvoiceDto getIssueInvoicePreview(Integer roomId, Date month) {
		Contract contract = contractRepository.findByRoomIdAndIsActive(roomId, true);
		Rooms room = contract.getRoom();
		Bills bill = null;
		List<BillServiceDto> services = getRoomService(contract, room, month, false);

		Double totalPrice = computeBills(contract, room, month, services);
		bill = generateBill(bill, room, month, contract, totalPrice);
		return convert2Dto(bill, room, contract, services);
	}

	@Override
	public Boolean confirmPayment(ConfirmInvoiceRequest request) {
		Boolean result = false;
		try {
			Bills bill = billRepository.findById(request.getInvoiceId()).orElse(null);
			bill.setPaidMoney(request.getPaidMoney());
			bill.setDebt(request.getDebt());
			bill.setIsPay(Boolean.TRUE);
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
		List<BillServiceDto> services = getRoomServiceAndCompute(contract, room, request.getReturnDate());
		Double totalPrice = computeBills(contract, room, request.getReturnDate(), services);

		Bills bill = null;
		bill = generateBill(bill, room, request.getReturnDate(), contract, totalPrice);

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
	

	@Override
	public void sendInvoiceEmail(Integer invoiceId, Date month) {
		invoiceThread.setInvoiceId(invoiceId);
		invoiceThread.setMonth(month);
		taskExecutor.execute(invoiceThread);
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
		result.setRoom(new RoomResponse(room.getId(), room.getName(), room.getPrice(), room.getCapacity()));
		return result;
	}

	@Override
	public Boolean removeInvoice(Integer invoiceId) {
		Boolean result = false;
		try {
			Bills bill = billRepository.findById(invoiceId).orElse(null);
			bill.setIsActive(Boolean.FALSE);
			billRepository.save(bill);
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
