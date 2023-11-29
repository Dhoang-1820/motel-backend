package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Equipments;
import com.petproject.motelservice.domain.query.response.EquipmentsResponse;

public interface EquipmentRepository extends JpaRepository<Equipments, Integer> {

	@Query(nativeQuery = true, value = "SELECT e.id , e.description, e.name, e.price, e.status, e.unit, null as roomId, e.accomodation_id as accomodationId, COUNT(*) as quantity FROM equipments e WHERE e.accomodation_id = :id GROUP BY name HAVING COUNT(e.id) > 0")
	List<EquipmentsResponse> findByAccomodationId(@Param("id") Integer accomodationId);
	
	List<Equipments> findByRoomId(@Param("roomId") Integer roomId);
	
	List<Equipments> findByNameAndAccomodationsId(String name, Integer accomodationId);
}	
