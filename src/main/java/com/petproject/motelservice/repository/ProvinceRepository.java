package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Province;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

	Province findByProvinceCode(Integer provinceCode);
	
	@Query("SELECT province FROM Province province INNER JOIN District district ON province.id = district.province.id INNER JOIN Ward ward ON ward.district.id = district.id INNER JOIN Address address ON address.ward.id = ward.id WHERE address.id IN ( SELECT accomodation.address.id FROM Accomodations accomodation INNER JOIN Rooms room ON accomodation.id = room.accomodations.id WHERE room.id IN (SELECT post.room.id FROM Post post WHERE post.isActive = true))")
	List<Province> findByPost();
}
