package com.petproject.motelservice.untils;


import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.petproject.motelservice.domain.dto.FileUploadDto;

@Component
public class StorageUntil {

	private static final Logger logger = LoggerFactory.getLogger(StorageUntil.class);

	@Value("${azure.storage.connection-string}")
	private String constr;

	@Value("${azure.storage.container}")
	private String containerName;

	public FileUploadDto uploadFile(MultipartFile multipartFile, String fileName, String contentType) {

		try {
			logger.info("Start file uploading process on AZ");
			
			File file = convertFile(multipartFile);

			BlobClient blobClient = null;

			// Create a BlobServiceClient object which will be used to create a container
			BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(constr)
			.buildClient();

			// Create the container and return
			// a container client object
			BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

			// Get a reference to a blob
			blobClient = containerClient.getBlobClient(file.getName());

			blobClient.uploadFromFile(file.getAbsolutePath());

			if (blobClient.getBlobUrl() != null) {
				logger.info("File successfully uploaded to AZ");
				Boolean isDeleteSuccess =  file.delete();
				if (isDeleteSuccess) {
					logger.info("Delete file in local after upload: {}", true);
				}
				return new FileUploadDto(blobClient.getBlobName(), blobClient.getBlobUrl());
			}

		} catch (Exception e) {
			logger.error("An error occurred while uploading data. Exception: ", e);
			throw new RuntimeException("An error occurred while storing data to AZ");
		}
		throw new RuntimeException("An error occurred while storing data to AZ");
	}

	private File convertFile(MultipartFile file) {

		try {
			if (file.getOriginalFilename() == null) {
				throw new RuntimeException("Original file name is null");
			}
			// file must be rename, except file duplicate
			String fileExtexsion = FilenameUtils.getExtension(file.getOriginalFilename());
			String generateFileName = UUID.randomUUID().toString().replace("-", "");
			generateFileName = generateFileName + "." + fileExtexsion;
			
			File convertedFile = new File(generateFileName);
			// save file in project
			FileOutputStream outputStream = new FileOutputStream(convertedFile);
			outputStream.write(file.getBytes());
			outputStream.close();
			logger.debug("Converting multipart file : {}", convertedFile);
			return convertedFile;
		} catch (Exception e) {
			throw new RuntimeException("An error has occurred while converting the file");
		}
	}

}
