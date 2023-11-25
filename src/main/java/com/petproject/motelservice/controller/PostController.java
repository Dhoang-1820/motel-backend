package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.ImageDto;
import com.petproject.motelservice.domain.dto.PostAddressDto;
import com.petproject.motelservice.domain.dto.PostDto;
import com.petproject.motelservice.domain.payload.request.PostRequest;
import com.petproject.motelservice.domain.payload.request.RangeRequest;
import com.petproject.motelservice.domain.payload.request.SearchByAddressRequest;
import com.petproject.motelservice.domain.payload.request.SearchPostRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.PostService;

@RestController
@RequestMapping(value = "/post")
public class PostController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/{accomodationId}/{userId}")
	public ResponseEntity<ApiResponse> getByUserIdAndAccomodation(@PathVariable Integer userId, @PathVariable Integer accomodationId) {
		final List<PostDto> result = postService.getPostByUserIdAndAccomodation(userId, accomodationId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse> getByUserId(@PathVariable Integer userId) {
		final List<PostDto> result = postService.getByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse> getAll() {
		final List<PostDto> result = postService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/address/all")
	public ResponseEntity<ApiResponse> getAllPostAddress() {
		final List<PostAddressDto> result = postService.getAllAddress();
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/address")
	public ResponseEntity<ApiResponse> getPostByAddress(@RequestBody SearchByAddressRequest request) {
		final List<PostDto> result = postService.getPostByAddress(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/price")
	public ResponseEntity<ApiResponse> getPostByRangeRoomPrice(@RequestBody RangeRequest request) {
		final List<PostDto> result = postService.getPostByPrice(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/areage")
	public ResponseEntity<ApiResponse> getPostByRangeRoomAreage(@RequestBody RangeRequest request) {
		final List<PostDto> result = postService.getPostByAreage(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/search")
	public ResponseEntity<ApiResponse> searchPost(@RequestBody SearchPostRequest request) {
		final List<PostDto> result = postService.searchPost(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ApiResponse> savePost(@RequestParam(value = "file", required = false) MultipartFile[] files, @RequestParam("data") String post) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		PostRequest request = mapper.readValue(post, PostRequest.class);
		final Boolean result = postService.savePost(request, files);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/image/{postId}")
	public ResponseEntity<ApiResponse> savePostImage(@RequestParam("file") MultipartFile[] files, @PathVariable("postId") Integer postId) {
		final List<String> result = postService.savePostImage(files, postId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/image/{postId}")
	public ResponseEntity<ApiResponse> getImageByPost(@PathVariable("postId") Integer postId) {
		final List<ImageDto> result = postService.getImageByPost(postId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping("/image/{imgId}")
	public ResponseEntity<ApiResponse> changeImageRoom(@RequestParam("file") MultipartFile[] files, @PathVariable("imgId") Integer imgId) {
		postService.changeRoomImage(files, imgId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/image/{imageId}")
	public ResponseEntity<ApiResponse> removeImage(@PathVariable("imageId") Integer imageId) {
		postService.removeImage(imageId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
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
