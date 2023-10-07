package com.petproject.motelservice.services;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.FileUploadDto;


public interface FileService {
	public List<FileUploadDto> uploadFiles(MultipartFile[] files);
}
