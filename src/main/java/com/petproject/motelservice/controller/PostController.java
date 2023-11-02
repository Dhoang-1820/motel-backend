package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.PostDto;
import com.petproject.motelservice.domain.payload.request.PostRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.PostService;

@RestController
@RequestMapping(value = "/post")
public class PostController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/{accomodationId}/{userId}")
	public ResponseEntity<ApiResponse> getByUserId(@PathVariable Integer userId, @PathVariable Integer accomodationId) {
		final List<PostDto> result = postService.getPostByUserId(userId, accomodationId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse> getAll() {
		final List<PostDto> result = postService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> savePost(@RequestBody PostRequest request) {
		final Boolean result = postService.savePost(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/image/{postId}")
	public ResponseEntity<ApiResponse> savePostImage(@RequestParam("file") MultipartFile[] files, @PathVariable("postId") Integer postId) {
		final List<String> result = postService.savePostImage(files, postId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping()
	public ResponseEntity<ApiResponse> changePostStatus(@RequestBody PostRequest request) {
		final Boolean result = postService.changePostStatus(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse> removePost(@PathVariable Integer postId) {
		final Boolean result = postService.removePost(postId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
}
