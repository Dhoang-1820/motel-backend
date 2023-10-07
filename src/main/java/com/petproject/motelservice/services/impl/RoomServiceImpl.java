package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.FileUploadDto;
import com.petproject.motelservice.domain.dto.ImageDto;
import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.dto.RoomImageDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Images;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.ImageRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.services.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	FileService storageService;
	
	@Autowired
	ModelMapper mapper;
	
	private static Log logger = LogFactory.getLog(RoomServiceImpl.class);

	@Override
	public List<RoomDto> getRoomsByAccomodation(Integer id) {
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodations(accomodations);
		List<RoomDto> result = rooms.stream()
                .map(source -> mapper.map(source, RoomDto.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<RoomDto> saveRoom(RoomDto request) {
		Rooms room = mapper.map(request, Rooms.class);
		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		room.setAccomodations(accomodations);
		room = roomRepository.save(room);
		return getRoomsByAccomodation(request.getAccomodationId());
	}

	@Override
	public void removeRoom(Integer roomId) {
		try {
			Rooms room = roomRepository.findById(roomId).orElse(null);
			if (room != null) {
				roomRepository.delete(room);			
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public List<RoomResponse> getRoomDropDown(Integer accomodationId) {
		Accomodations accomodations = accomodationsRepository.findById(accomodationId).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodations(accomodations);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<RoomServiceResponse> getRoomNotHasService(Integer accomodationId) {
		List<RoomServiceResponse> result = roomRepository.findRoomNotHasService(accomodationId);
		return result;
	}
	
	@Override
	public List<RoomResponse> getRoomNoDeposit(Integer accomodationId) {
		List<Rooms> rooms = roomRepository.findRoomNoDepostit(accomodationId);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}
	
	@Override
	public RoomImageDto getRoomImages(Integer roomId) {
		Rooms room = roomRepository.findById(roomId).orElse(null);
//		List<Images> images = room.getImages();
		RoomImageDto result = new RoomImageDto();
		result.setRoomId(roomId);
		List<ImageDto> dto = new ArrayList<>();
		ImageDto imgDto = null;
//		for (Images img : images) {
//			imgDto = new ImageDto();
//			imgDto.setImageId(img.getId());
//			imgDto.setImgName(img.getImageName());
//			imgDto.setImgUrl(img.getImageUrl());
//			dto.add(imgDto);
//		}
		result.setImages(dto);
		return result;
	}

	@Override
	public void removeImage(Integer imageId) {
		Images image = imageRepository.findById(imageId).orElse(null);
		imageRepository.delete(image);
	}
	
	@Override
	public void saveRoomImage(MultipartFile[] images, Integer roomId) {
		List<Images> roomImg = new ArrayList<>();
		Images img = null;
		Rooms room = roomRepository.findById(roomId).orElse(null);
		if (images != null) {
			List<FileUploadDto> imgResult = storageService.uploadFiles(images);
			for (FileUploadDto item : imgResult) {
				img = new Images();
				img.setCreatedAt(new Date());
				img.setImageName(item.getFileName());
				img.setImageUrl(item.getFileUrl());
//				img.setRoom(room);
				roomImg.add(img);
			}
		}
		roomImg = imageRepository.saveAll(roomImg);
	}

	@Override
	public void changeRoomImage(MultipartFile[] images, Integer imageId) {
		List<FileUploadDto> imgResult = storageService.uploadFiles(images);
		Images img = imageRepository.findById(imageId).orElse(null);
		img.setImageUrl(imgResult.get(0).getFileUrl());
		imageRepository.save(img);
	}
	
	
}
