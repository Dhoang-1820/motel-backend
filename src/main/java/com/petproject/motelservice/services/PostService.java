package com.petproject.motelservice.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.PostDto;
import com.petproject.motelservice.domain.payload.request.PostRequest;


public interface PostService {
	
	List<PostDto> getAll();
	
	List<PostDto> getPostByUserId(Integer userId, Integer accomodationId);
	
	Boolean savePost(PostRequest request);
	
	Boolean changePostStatus(PostRequest request);
	
	Boolean removePost(Integer postId);
	
	List<String> savePostImage(MultipartFile[] images, Integer postId);
}
