package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Bills;
import com.petproject.motelservice.domain.query.response.InvoiceResponse;

public interface BillRepository extends JpaRepository<Bills, Integer> {
	
	@Query("FROM Bills bill WHERE month(bill.billDate) = month(:month) AND year(bill.billDate) = year(:month) AND bill.room.id = :roomId AND bill.invoiceType.id = 1 AND bill.isActive = true")
	Bills findByMonthAndRoom(@Param("month") Date month, @Param("roomId") Integer roomId);

	@Query(nativeQuery = true, value = "select rooms.id as roomId, rooms.name as roomName, bill.total_payment as totalPayment, bill.paid_money as paidMoney, bill.quantity_sent as quantitySent, bill.discount as discount, bill.debt as debt, bill.payment_date as paymentDate, bill.is_pay as isPay, if(bill.id is not null, (select concat(tenants.first_name, ' ',tenants.last_name ) from tenants where id = contract.representative), null) as representative, bill.id as billId from (select rooms.* from rooms join contract on rooms.id = contract.room_id where month(start_date) < month(:month) AND year(start_date) = year(:month) and contract.end_date >= curdate() and contract.is_active = 1) rooms left join (select * from bills where month(date) = month(:month) AND year(date) = year(:month) and invoice_type_id = 1 and is_active = 1) bill on rooms.id = bill.room_id left join contract on rooms.id = contract.room_id where rooms.accomodation_id = :accomodationId and rooms.is_rent = 1")
	List<InvoiceResponse> findCurrentInvoiceByMonth(@Param("accomodationId") Integer accomodationId, Date month);
	
	@Query("SELECT bill.debt FROM Bills bill where bill.room.id = :roomId AND month(bill.billDate) = month(:month) AND year(bill.billDate) = year(:month) AND bill.invoiceType.id = 1")
	Double findDebtByRoom(Integer roomId, Date month);
	
	@Query(nativeQuery = true, value = "select rooms.id as roomId, rooms.name as roomName, bill.total_payment as totalPayment, bill.total_price as totalPrice, bill.paid_money as paidMoney, bill.quantity_sent as quantitySent, bill.discount as discount, bill.debt as debt, bill.payment_date as paymentDate, bill.is_pay as isPay, if(bill.id is not null, (select concat(tenants.first_name, ' ',tenants.last_name ) from tenants where id = contract.representative), null) as representative, (select end_date from tenants where id = contract.representative) as returnDate, bill.id as billId from rooms join (select * from bills where month(date) = month(:month) AND year(date) = year(:month) and invoice_type_id = 2  and is_active = 1) bill on rooms.id = bill.room_id left join contract on rooms.id = contract.room_id where rooms.accomodation_id = :accomodationId and contract.is_active = 1")
	List<InvoiceResponse> findCurrentReturnInvoiceByMonth(@Param("accomodationId") Integer accomodationId, Date month);
	
	@Query("FROM Bills bill INNER JOIN Rooms room ON bill.room.id = room.id WHERE room.accomodations.id = :accomodationId AND bill.isActive = true AND YEAR(bill.billDate) = YEAR(:year) GROUP BY MONTH(bill.billDate) ORDER BY MONTH(bill.billDate)")
	List<Bills> findBillRevenueByUserId(Integer accomodationId, Date year);
	
}
