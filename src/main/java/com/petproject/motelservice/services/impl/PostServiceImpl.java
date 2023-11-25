package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.DistrictDto;
import com.petproject.motelservice.domain.dto.FileUploadDto;
import com.petproject.motelservice.domain.dto.ImageDto;
import com.petproject.motelservice.domain.dto.PostAddressDto;
import com.petproject.motelservice.domain.dto.PostDto;
import com.petproject.motelservice.domain.dto.WardDto;
import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.District;
import com.petproject.motelservice.domain.inventory.EPostStatus;
import com.petproject.motelservice.domain.inventory.Images;
import com.petproject.motelservice.domain.inventory.Post;
import com.petproject.motelservice.domain.inventory.PostStatus;
import com.petproject.motelservice.domain.inventory.Province;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.inventory.Ward;
import com.petproject.motelservice.domain.payload.request.PostRequest;
import com.petproject.motelservice.domain.payload.request.PostStatusRequest;
import com.petproject.motelservice.domain.payload.request.RangeRequest;
import com.petproject.motelservice.domain.payload.request.SearchByAddressRequest;
import com.petproject.motelservice.domain.payload.request.SearchPostRequest;
import com.petproject.motelservice.repository.AccomodationServiceRepository;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.AddressRepository;
import com.petproject.motelservice.repository.DistrictRepository;
import com.petproject.motelservice.repository.ImageRepository;
import com.petproject.motelservice.repository.PostRepository;
import com.petproject.motelservice.repository.PostStatusRepository;
import com.petproject.motelservice.repository.ProvinceRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.repository.WardRepository;
import com.petproject.motelservice.services.EquipmentService;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	AccomodationsRepository accomodationsRepository;

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	EquipmentService equipmentService;

	@Autowired
	AccomodationServiceRepository accomodationServiceRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	FileService storageService;

	@Autowired
	WardRepository wardRepository;

	@Autowired
	DistrictRepository districtRepository;

	@Autowired
	ProvinceRepository provinceRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	PostStatusRepository postStatusRepository;

	private static final Log logger = LogFactory.getLog(PostServiceImpl.class);

	@Override
	public List<String> savePostImage(MultipartFile[] images, Integer postId) {
		List<Images> roomImg = new ArrayList<>();
		Images img = null;
		Post post = postRepository.findById(postId).orElse(null);
		if (images != null) {
			List<FileUploadDto> imgResult = storageService.uploadFiles(images);
			for (FileUploadDto item : imgResult) {
				img = new Images();
				img.setCreatedAt(new Date());
				img.setImageName(item.getFileName());
				img.setImageUrl(item.getFileUrl());
				img.setPost(post);
				roomImg.add(img);
			}
		}
		roomImg = imageRepository.saveAll(roomImg);
		List<String> result = new ArrayList<>();
		for (Images item : roomImg) {
			result.add(item.getImageUrl());
		}
		return result;
	}

	@Override
	public List<PostDto> getAll() {
		List<PostDto> result = new ArrayList<>();
		List<Post> posts = postRepository.findActivePost();
		for (Post post : posts) {
			result.add(convert2Dto(post));
		}
		return result;
	}

	@Override
	public List<PostDto> getPostByUserIdAndAccomodation(Integer userId, Integer accomodationId) {
		List<PostDto> result = new ArrayList<>();
		try {
			List<Post> posts = postRepository.findByUserIdAndAccomodationId(userId, accomodationId);
			for (Post post : posts) {
				result.add(convert2Dto(post));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<PostDto> getByUserId(Integer userId) {
		List<PostDto> result = new ArrayList<>();
		try {
			List<Post> posts = postRepository.findByUserId(userId);
			for (Post post : posts) {
				result.add(convert2Dto(post));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<ImageDto> getPostImages(Post post) {
		List<ImageDto> result = new ArrayList<>();
		ImageDto dto = null;
		List<Images> images = post.getImages();
		for (Images img : images) {
			dto = new ImageDto();
			dto.setImageId(img.getId());
			dto.setImgName(img.getImageName());
			dto.setImgUrl(img.getImageUrl());
			result.add(dto);
		}
		return result;
	}

	private PostDto convert2Dto(Post post) {
		PostDto dto = null;
		Address address = null;
		String addressLine = null;
		List<ImageDto> images = null;
		Users user = usersRepository.findByUserId(post.getUser().getId());

		dto = new PostDto();
		dto.setId(post.getId());
		dto.setTitle(post.getTitle());
		dto.setContent(post.getContent());
		dto.setAcreage(post.getAcreage());
		dto.setPrice(post.getPrice());
		dto.setCapacity(post.getCapacity());
		dto.setStatus(post.getPostStatus().getName());
		address = post.getAddress();
		Ward ward = address.getWard();
		District district = ward.getDistrict();
		Province province = district.getProvince();
		addressLine = address.getAddressLine() + ", " + ward.getWard() + ", " + district.getDistrict() + ", "
				+ province.getProvince();
		dto.setAddress(addressLine);

		if (address != null) {
			dto.setAddressId(address.getId());
			dto.setAddressLine(address.getAddressLine());
			dto.setDistrict(district.getDistrict());
			dto.setDistrictCode(district.getDistrictCode());
			dto.setProvince(province.getProvince());
			dto.setProvinceCode(province.getProvinceCode());
			dto.setWard(ward.getWard());
			dto.setWardCode(ward.getWardCode());
		}
		dto.setPhone(user.getPhone());
		images = getPostImages(post);
		dto.setImages(images);
		dto.setCreatedAt(post.getCreatedAt());
		dto.setIsActive(post.getIsActive());
		return dto;
	}

	@Override
	public Boolean savePost(PostRequest request, MultipartFile[] images) {
		Boolean result = false;
		Post post = null;
		Address address = null;
		try {
			if (request.getId() != null) {
				post = postRepository.findById(request.getId()).orElse(null);
				address = post.getAddress();
				if (address == null) {
					address = new Address();
				}
				post.setLastChange(new Date());
			} else {
				post = new Post();
				address = new Address();
				post.setCreatedAt(new Date());
				PostStatus postStatus = postStatusRepository.findByName(EPostStatus.IN_PROGRESS);
				post.setPostStatus(postStatus);
				post.setIsActive(Boolean.TRUE);
			}
			Users user = usersRepository.findByUserId(request.getUserId());
			post.setTitle(request.getTitle());
			post.setContent(request.getContent());
			post.setUser(user);
			post.setAcreage(request.getAcreage());
			post.setPrice(request.getPrice());
			post.setCapacity(request.getCapacity());
			Ward ward = wardRepository.findByWardCode(request.getWardCode());
			if (ward == null) {
				ward = new Ward();
				ward.setWard(request.getWard());
				ward.setWardCode(request.getWardCode());
				District district = districtRepository.findByDistrictCode(request.getDistrictCode());
				if (district == null) {
					district = new District();
					district.setDistrict(request.getDistrict());
					district.setDistrictCode(request.getDistrictCode());
					Province province = provinceRepository.findByProvinceCode(request.getProvinceCode());
					if (province == null) {
						province = new Province();
						province.setProvince(request.getProvince());
						province.setProvinceCode(request.getProvinceCode());
						province = provinceRepository.save(province);
					}
					district.setProvince(province);
					district = districtRepository.save(district);
				}
				ward.setDistrict(district);
				ward = wardRepository.save(ward);
			}
			address.setAddressLine(request.getAddressLine());
			address.setWard(ward);
			address = addressRepository.save(address);
			post.setAddress(address);
			post = postRepository.save(post);

			List<Images> roomImg = new ArrayList<>();
			Images img = null;
			if (images != null) {
				List<FileUploadDto> imgResult = storageService.uploadFiles(images);
				for (FileUploadDto item : imgResult) {
					img = new Images();
					img.setCreatedAt(new Date());
					img.setImageName(item.getFileName());
					img.setImageUrl(item.getFileUrl());
					img.setPost(post);
					roomImg.add(img);
				}
			}
			roomImg = imageRepository.saveAll(roomImg);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean changePostStatus(PostStatusRequest request) {
		Boolean result = false;
		try {
			Post post = postRepository.findById(request.getPostId()).orElse(null);
			if (post != null) {
				PostStatus status = postStatusRepository.findByName(request.getStatus());
				post.setPostStatus(status);
				postRepository.save(post);
			}
			postRepository.save(post);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean removePost(Integer postId) {
		Boolean result = false;
		try {
			Post post = postRepository.findById(postId).orElse(null);
			List<Images> images = post.getImages();
			imageRepository.deleteAll(images);
			postRepository.delete(post);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void changeRoomImage(MultipartFile[] images, Integer imageId) {
		List<FileUploadDto> imgResult = storageService.uploadFiles(images);
		Images img = imageRepository.findById(imageId).orElse(null);
		img.setImageUrl(imgResult.get(0).getFileUrl());
		imageRepository.save(img);
	}

	@Override
	public List<ImageDto> getImageByPost(Integer postId) {
		Post post = postRepository.findById(postId).orElse(null);
		return getPostImages(post);
	}

	@Override
	public void removeImage(Integer imageId) {
		Images image = imageRepository.findById(imageId).orElse(null);
		imageRepository.delete(image);
	}

	@Override
	public List<PostAddressDto> getAllAddress() {
		List<PostAddressDto> result = new ArrayList<>();
		PostAddressDto dto = new PostAddressDto();
		dto.setName("Toàn quốc");
		dto.setCode(1001);
		result.add(dto);
		DistrictDto districtDto = null;
		List<DistrictDto> districtDtos = null;
		WardDto wardDto = null;
		List<WardDto> wardDtos = null;
		List<Province> provinces = provinceRepository.findByPost();
		List<District> districts;
		List<Ward> wards;
		for (Province province : provinces) {
			dto = new PostAddressDto();
			dto.setName(province.getProvince());
			dto.setCode(province.getProvinceCode());
			districts = province.getDistricts();
			districtDtos = new ArrayList<>();
			districtDto = new DistrictDto();
			districtDto.setName("Tất cả");
			districtDto.setCode(1002);
			districtDto.setParentCode(province.getProvinceCode());
			districtDtos.add(districtDto);
			for (District district : districts) {
				districtDto = new DistrictDto();
				districtDto.setName(district.getDistrict());
				districtDto.setCode(district.getDistrictCode());
				districtDtos.add(districtDto);
				wards = district.getWards();
				wardDtos = new ArrayList<>();
				wardDto = new WardDto();
				wardDto.setName("Tất cả");
				wardDto.setCode(1003);
				wardDto.setParentCode(district.getDistrictCode());
				wardDtos.add(wardDto);
				for (Ward ward : wards) {
					wardDto = new WardDto();
					wardDto.setName(ward.getWard());
					wardDto.setCode(ward.getWardCode());
					wardDtos.add(wardDto);
				}
				districtDto.setWards(wardDtos);
			}
			dto.setDistricts(districtDtos);
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<PostDto> getPostByAddress(SearchByAddressRequest request) {
		List<PostDto> result = new ArrayList<>();

		Province province = null;
		District district = null;
		Ward ward = null;
		List<District> districts = null;
		List<Ward> wards = null;
		List<Address> addresses = new ArrayList<>();

		if (request.getLevel().equals(1)) {
			if (!request.getCode().equals(1001)) {
				province = provinceRepository.findByProvinceCode(request.getCode());
				districts = province.getDistricts();
				for (District districtItem : districts) {
					wards = districtItem.getWards();
					for (Ward wardItem : wards) {
						addresses.addAll(wardItem.getAddresses());
					}
				}
			} else {
				result = getAll();
			}

		} else if (request.getLevel().equals(2)) {
			if (!request.getCode().equals(1002)) {
				district = districtRepository.findByDistrictCode(request.getCode());
				wards = district.getWards();
				for (Ward wardItem : wards) {
					addresses = wardItem.getAddresses();
				}
			} else {
				province = provinceRepository.findByProvinceCode(request.getParentCode());
				districts = province.getDistricts();
				for (District districtItem : districts) {
					wards = districtItem.getWards();
					for (Ward wardItem : wards) {
						addresses.addAll(wardItem.getAddresses());
					}
				}
			}

		} else if (request.getLevel().equals(3)) {
			if (!request.getCode().equals(1003)) {
				ward = wardRepository.findByWardCode(request.getCode());
				addresses = ward.getAddresses();
			} else {
				district = districtRepository.findByDistrictCode(request.getParentCode());
				wards = district.getWards();
				for (Ward wardItem : wards) {
					addresses.addAll(wardItem.getAddresses());
				}
			}

		}
		List<Post> posts = postRepository.findByAddressId(addresses);
		for (Post post : posts) {
			result.add(convert2Dto(post));
		}
		return result;
	}

	@Override
	public List<PostDto> getPostByPrice(RangeRequest request) {
		List<PostDto> result = new ArrayList<>();
		List<Post> posts = postRepository.findByRangeRoomPrice(request.getFrom(), request.getTo());
		for (Post post : posts) {
			result.add(convert2Dto(post));
		}
		return result;
	}

	@Override
	public List<PostDto> getPostByAreage(RangeRequest request) {
		List<PostDto> result = new ArrayList<>();
		List<Post> posts = postRepository.findByRangeRoomAreage(request.getFrom(), request.getTo());
		for (Post post : posts) {
			result.add(convert2Dto(post));
		}
		return result;
	}

	@Override
	public List<PostDto> searchPost(SearchPostRequest request) {
		List<PostDto> result = new ArrayList<>();
		Set<PostDto> addressSet = new HashSet<>();
		Set<PostDto> priceSet = new HashSet<>();
		Set<PostDto> areageSet = new HashSet<>();
		Set<PostDto> finalSet = new HashSet<>();

		if (request.getAddress() != null) {
			addressSet.addAll(getPostByAddress(request.getAddress()));
			finalSet.addAll(addressSet);
		}
		if (request.getPrice() != null) {
			priceSet.addAll(getPostByPrice(request.getPrice()));
			if (!finalSet.isEmpty()) {
				finalSet.retainAll(priceSet);
			} else {
				finalSet.addAll(priceSet);
			}
		}
		if (request.getAreage() != null) {
			areageSet.addAll(getPostByAreage(request.getAreage()));
			if (!finalSet.isEmpty()) {
				finalSet.retainAll(areageSet);
			} else {
				finalSet.addAll(areageSet);
			}
		}

		result = new ArrayList<>(finalSet);
		return result;
	}

	@Override
	public List<PostDto> getByStatus(PostStatusRequest request) {
		List<PostDto> result = new ArrayList<>();
		List<Post> posts = null;
		if (request.getStatus() != null) {
			posts = postRepository.findByPostStatusName(request.getStatus());
		} else {
			posts = postRepository.findAll();
		}
		
		for (Post post : posts) {
			result.add(convert2Dto(post));
		}
		return result;
	}

}
