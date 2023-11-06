package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("SELECT post FROM Post post INNER JOIN Users user ON post.user.id = user.id INNER JOIN Accomodations accomodations ON accomodations.user.id = user.id WHERE user.id = :userId AND accomodations.id = :accomodationId")
	List<Post> findByUserIdAndAccomodationId(Integer userId, Integer accomodationId);
	
	@Query("FROM Post post WHERE post.isActive = true")
	List<Post> findActivePost();
	
	@Query("SELECT post FROM Post post INNER JOIN Rooms room ON post.room.id = room.id INNER JOIN Accomodations accomodations ON accomodations.id = room.accomodations.id WHERE  post.isActive = true AND accomodations.address IN (:address)")
	List<Post> findByAddressId(List<Address> address);
	
	@Query("SELECT post FROM Post post INNER JOIN Rooms room ON post.room.id = room.id WHERE room.price BETWEEN :from AND :to AND post.isActive = true")
	List<Post> findByRangeRoomPrice(Double from, Double to);
	
	@Query("SELECT post FROM Post post INNER JOIN Rooms room ON post.room.id = room.id WHERE room.acreage BETWEEN :from AND :to AND post.isActive = true")
	List<Post> findByRangeRoomAreage(Double from, Double to);
	
	List<Post> findByRoomIdAndIsActive(Integer roomId, Boolean isActive);
}
