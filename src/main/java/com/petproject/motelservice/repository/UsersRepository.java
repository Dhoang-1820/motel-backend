package com.petproject.motelservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.query.response.UserResponse;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByUsername(String usename);
	
	Boolean existsByUsername (String username);
	
	Users findByEmail(String email);
	
	@Query("SELECT user FROM Users user where user.id = :userId")
	Users findByUserId(@Param("userId") Integer id);
	
	@Query(nativeQuery = true, value = "select users.id as userId, users.address, users.username as userName, users.phone, users.email, users.firstname as firstName, users.lasname as lastName, users.identify_num as identifyNum, users.active, users.created_at as createdAt, case when accomodation.accomodationNum > 0 then accomodation.accomodationNum else 0 end as accomodationNum, role.name as role from users left join (select count(accomodations.user_id) as accomodationNum, accomodations.user_id from accomodations group by accomodations.user_id) accomodation on users.id = accomodation.user_id join role on users.role_id = role.id where role.name != 'ROLE_ADMIN'")
	List<UserResponse> findAllUsers();
		
}
