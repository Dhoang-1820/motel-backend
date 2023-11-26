package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.EPostStatus;
import com.petproject.motelservice.domain.inventory.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("SELECT post FROM Post post INNER JOIN Users user ON post.user.id = user.id INNER JOIN Accomodations accomodations ON accomodations.user.id = user.id WHERE user.id = :userId AND accomodations.id = :accomodationId AND post.postStatus.name != 'REMOVED'")
	List<Post> findByUserIdAndAccomodationId(Integer userId, Integer accomodationId);
	
	@Query("FROM Post post WHERE post.isActive = true AND post.postStatus.name = 'APPROVED'")
	List<Post> findActivePost();
	
	@Query("SELECT post FROM Post post WHERE post.isActive = true AND post.address IN (:address) AND post.postStatus.name != 'REJECTED' OR post.postStatus.name != 'REMOVED'")
	List<Post> findByAddressId(List<Address> address);
	
	@Query("SELECT post FROM Post post WHERE post.price BETWEEN :from AND :to AND post.isActive = true AND post.postStatus.name != 'REJECTED' OR post.postStatus.name != 'REMOVED'")
	List<Post> findByRangeRoomPrice(Double from, Double to);
	
	@Query("SELECT post FROM Post post WHERE post.acreage BETWEEN :from AND :to AND post.isActive = true AND post.postStatus.name != 'REJECTED' OR post.postStatus.name != 'REMOVED'")
	List<Post> findByRangeRoomAreage(Double from, Double to);
	
	@Query("SELECT post FROM Post post WHERE post.user.id = :userId AND post.isActive = true")
	List<Post> findByUserId(Integer userId);
	
	@Query("SELECT post FROM Post post WHERE post.postStatus.name = :postStatus AND post.postStatus.name != 'REMOVED' AND post.isActive = true")
	List<Post> findByPostStatusName(EPostStatus postStatus);
	
//	List<Post> findByRoomIdAndIsActive(Integer roomId, Boolean isActive);
}
