package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("SELECT post FROM Post post INNER JOIN Users user ON post.user.id = user.id INNER JOIN Accomodations accomodations ON accomodations.user.id = user.id WHERE user.id = :userId AND accomodations.id = :accomodationId")
	List<Post> findByUserIdAndAccomodationId(Integer userId, Integer accomodationId);
	
	@Query("FROM Post post WHERE post.isActive = true")
	List<Post> findActivePost();
}
