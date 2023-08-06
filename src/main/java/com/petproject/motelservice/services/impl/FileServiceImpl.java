package com.petproject.motelservice.services.impl;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.FileUploadDto;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.untils.StorageUntil;



@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	private StorageUntil storageUntil;
	
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	private boolean isImageFile(MultipartFile file) {
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
		return Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp" }).contains(fileExtension.trim().toLowerCase());
	}

	@Override
	public List<FileUploadDto> uploadFiles(MultipartFile[] files) {
		logger.info("Start file uploading service");
		List<FileUploadDto> result = new ArrayList<>();
		Arrays.asList(files).forEach(file -> {
			if (!isImageFile(file)) {
				logger.error("Not a permitted file type");
				throw new RuntimeException("Not a permitted file type, Just only image file");
			}

			String originalFileName = file.getOriginalFilename();
			if (originalFileName == null) {
				throw new RuntimeException("Original file name is null");
			}
			Path path = new File(originalFileName).toPath();

			try {
				FileUploadDto fileDto =  new FileUploadDto();
				String contentType = Files.probeContentType(path);
				fileDto = storageUntil.uploadFile(file, originalFileName, contentType);
				result.add(fileDto);
				if (fileDto != null) {
					logger.info("File uploaded successfully, file name: {} and url: {}", fileDto.getFileName(), fileDto.getFileUrl());
				}
			} catch (Exception e) {
				logger.error("Error occurred while uploading. Error: ", e);
				throw new RuntimeException("Error occurred while uploading");
			}
		});
		return result;
	}

}
