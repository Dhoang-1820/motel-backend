package com.petproject.motelservice.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.BankAccountDto;
import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.dto.DashBoardDto;
import com.petproject.motelservice.domain.dto.FileUploadDto;
import com.petproject.motelservice.domain.dto.UserDto;
import com.petproject.motelservice.domain.dto.UserPreferenceDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.BankAccountInfo;
import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.RefreshToken;
import com.petproject.motelservice.domain.inventory.Role;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.inventory.UserPreference;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.request.ChangePasswordRequest;
import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.domain.payload.request.TokenRefreshRequest;
import com.petproject.motelservice.domain.payload.request.UpdateUserRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.JwtResponse;
import com.petproject.motelservice.domain.payload.response.TokenRefreshResponse;
import com.petproject.motelservice.domain.query.response.UserResponse;
import com.petproject.motelservice.repository.BankAccountRepository;
import com.petproject.motelservice.repository.RolesRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.repository.UserPreferenceRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.security.jwt.JwtUtils;
import com.petproject.motelservice.security.services.RefreshTokenService;
import com.petproject.motelservice.security.services.UserDetailsImpl;
import com.petproject.motelservice.services.BookingService;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	FileService storageService;
	
	@Autowired
	BookingService bookingService;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	BankAccountRepository bankAccountRepository;
	
	@Autowired
	UserPreferenceRepository preferenceRepository;

	@Override
	public JwtResponse signIn(LoginRequest loginRequest) {	
		JwtResponse jwtResponse = new JwtResponse();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		String roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList()).get(0);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		RefreshToken refreshToken = refreshTokenService.getByUserId(userDetails.getId());
		if (refreshToken != null ) {
			refreshTokenService.deleteByUserId(userDetails.getId());
		}
		refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		
		jwtResponse.setId(userDetails.getId());
		jwtResponse.setEmail(userDetails.getEmail());
		jwtResponse.setIsActive(userDetails.getIsActive());
		jwtResponse.setUsername(userDetails.getUsername());
		jwtResponse.setRefreshToken(refreshToken.getToken());
		jwtResponse.setToken(jwt);
		jwtResponse.setRoles(roles);
		jwtResponse.setFirstname(userDetails.getFirstname());
		jwtResponse.setLastname(userDetails.getLastname());
		jwtResponse.setIdentifyNum(userDetails.getIdentifyNum());
		jwtResponse.setPhone(userDetails.getPhone());
		return jwtResponse;
	}

	@Override
	public TokenRefreshResponse getRefreshtoken(TokenRefreshRequest request) {
		TokenRefreshResponse refreshResponse = new TokenRefreshResponse();
		String requestRefreshToken = request.getRefreshToken();
		RefreshToken refreshToken = refreshTokenService.getByToken(requestRefreshToken).orElse(null);
		if (refreshToken != null) {
			refreshToken = refreshTokenService.verifyExpiration(refreshToken);
			Users user = refreshToken.getUser();
			String token = jwtUtils.generateTokenFromUsername(user.getUsername());
			refreshResponse.setAccessToken(token);
			refreshResponse.setRefreshToken(refreshToken.getToken());
		}
		return refreshResponse;
	}

	@Override
	public ApiResponse signUp(SignupRequest signUpRequest) {
		Users user = new Users();
		ApiResponse response = new ApiResponse();
		user.setEmail(signUpRequest.getEmail());
		user.setAddress(signUpRequest.getAddress());
		user.setUsername(signUpRequest.getUserName());
		user.setPhone(signUpRequest.getPhone());
		user.setFirstname(signUpRequest.getFirstName());
		user.setLastname(signUpRequest.getLastName());
		user.setActive(Boolean.TRUE);
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

		String strRoles = signUpRequest.getRoles();
		Role role = null;
		
		switch (strRoles) {
			case "admin":
				role = rolesRepository.findByName(ERoles.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				break;
			case "landlord":
				role = rolesRepository.findByName(ERoles.ROLE_LANDLORD)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				break;
			case "poster":
				role = rolesRepository.findByName(ERoles.ROLE_POSTER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				break;
			default:
				role = rolesRepository.findByName(ERoles.ROLE_LANDLORD)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		}

		user.setRole(role);
		user.setCreatedAt(new Date());
		usersRepository.save(user);
		response.setData(user);
		response.setMessage(Constants.SIGNUP_SUCCESS_MSG);
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	

	@Override
	public Boolean checkDuplicateEmail(String email) {
		Users user = usersRepository.findByEmail(email);
		return user != null;
	}
	
	@Override
	public Boolean checkDuplicateUsername(String userName) {
		Users user = usersRepository.findByUsername(userName).orElse(null);
		return user != null;
	}

	@Override
	public UserDto getUserByUserId(Integer userId) {
		UserDto result = new UserDto();
		Users user = usersRepository.findByUserId(userId);
		result = mapper.map(user, UserDto.class);
		List<BankAccountInfo> accountInfos = user.getBankAccountInfos();
		BankAccountDto accountDto = null;
		List<BankAccountDto> banks = new ArrayList<>();
		for (BankAccountInfo bank : accountInfos) {
			accountDto = new BankAccountDto();
			accountDto.setAccountOwner(bank.getAccountOwner());
			accountDto.setBankName(bank.getBankName());
			accountDto.setBankNumber(bank.getBankNumber());
			accountDto.setId(bank.getId());
			banks.add(accountDto);
		}
		result.setBankAccounts(banks);
		return result;
	}
	

	@Override
	public UserPreferenceDto getUserConfigByUserId(Integer userId) {
		Users user =  usersRepository.findByUserId(userId);
		UserPreference preference = user.getUserPreference();
		UserPreferenceDto dto = convert2UserPreference(preference);
		return dto;
	}
	
	private UserPreferenceDto convert2UserPreference(UserPreference preference ) {
		UserPreferenceDto dto = new UserPreferenceDto();
		dto.setElectricWaterDate(preference.getEletricWaterDate());
		dto.setIssueInvoiceDate(preference.getIssueInvoiceDate());
		dto.setId(preference.getId());
		dto.setUserId(preference.getUser().getId());
		return dto;
	}
	
	@Override
	public UserPreferenceDto updateUserPreference(UserPreferenceDto request) {
		UserPreference preference = null;
		if (request.getId() != null) {
			preference = preferenceRepository.findById(request.getId()).orElse(null);
		} else {
			preference = new UserPreference();
		}
		
		try {
			preference.setEletricWaterDate(request.getElectricWaterDate());
			preference.setIssueInvoiceDate(request.getIssueInvoiceDate());
			preference = preferenceRepository.save(preference);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convert2UserPreference(preference);
	}
	
	@Override
	public DashBoardDto getUserDashboard(Integer userId) {
		DashBoardDto dto = new DashBoardDto();
		Users user = usersRepository.findByUserId(userId);
		List<Accomodations> accomodations = user.getAccomodations();
		dto.setAccomodationNum(accomodations.size());
		int accomodationNum = 0;
		int roomNum = 0;
		int emptyRoomNum = 0;
		int tenantCount = 0;
		List<Rooms> rooms = null;
		for (Accomodations item : accomodations) {
			if (item.getIsActive().equals(Boolean.TRUE)) {
				rooms = item.getRooms();
				accomodationNum += 1;
				roomNum += rooms.size();
				emptyRoomNum += getEmptyRoom(rooms);
			}
		}
		List<Tenants> tenants = tenantRepository.countTenantByUserId(userId);
		tenantCount = tenants.size();
		dto.setAccomodationNum(accomodationNum);
		dto.setRoomNum(roomNum);
		dto.setTenantNum(tenantCount);
		dto.setEmptyRoomNum(emptyRoomNum);
		List<String> notifications = new ArrayList<>();
		List<BookingDto> bookings = bookingService.getBookingByDate(userId, new Date());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String notification = "";
		for (BookingDto booking : bookings) {
			notification = dateFormat.format(booking.getCreatedDate()).toString();
			notifications.add(notification);
		}
		dto.setNotifications(notifications);
		return dto;
	}
	
	private int getEmptyRoom(List<Rooms> rooms) {
		int result = 0;
		for (Rooms room : rooms) {
			if (!room.getIsRent()) {
				result += 1;
			}
		}
		return result;
	}

	@Override
	public UserDto createOrUpdate(UserDto request, MultipartFile[] image) {
		Users user = null;
		if (request.getId() == null) {
			user = new Users();
		} else {
			user = usersRepository.findById(request.getId()).orElse(null);
		}
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setEmail(request.getEmail());
		user.setAddress(request.getAddress());
		user.setPhone(request.getPhone());
		user.setIdentifyNum(request.getIdentifyNum());
		
		if (image != null) {
			List<FileUploadDto> imgResult = storageService.uploadFiles(image);
			user.setImageUrl(imgResult.get(0).getFileUrl());			
		}
		usersRepository.save(user);
		UserDto result = mapper.map(user, UserDto.class);
		return result;
	}
	

	@Override
	public Users getUserById(Integer userId) {
		Users user = usersRepository.findById(userId).orElse(null);
		return user;
	}


	@Override
	public BankAccountDto saveBankAccount(BankAccountDto request) {
		BankAccountDto result = null;
		BankAccountInfo accountInfo = new BankAccountInfo();
		if (request.getId() != null) {
			accountInfo = bankAccountRepository.findById(request.getId()).orElse(null);
		}
		accountInfo.setAccountOwner(request.getAccountOwner());
		accountInfo.setBankName(request.getBankName());
		accountInfo.setBankNumber(request.getBankNumber());
		Users user = getUserById(request.getUserId());
		accountInfo.setUser(user);
		bankAccountRepository.save(accountInfo);
		result = mapper.map(accountInfo, BankAccountDto.class);
		return result;
	}
	

	@Override
	public void removeBankAccount(Integer bankId) {
		try {
			BankAccountInfo accountInfo = bankAccountRepository.findById(bankId).orElse(null);
			bankAccountRepository.delete(accountInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest) {
		ApiResponse response = new ApiResponse();
		String oldPassword = changePasswordRequest.getOldPassword();
		Users user = usersRepository.findById(changePasswordRequest.getUserId()).orElse(null);
		if (encoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
			response.setSuccess(true);
			user = usersRepository.save(user);
		} else {
			response.setSuccess(false);
			response.setMessage("Mật khẩu cũ không chính xác");
		}
		
		return response;
	}

	@Override
	public List<UserResponse> getAllUser() {
		return usersRepository.findAllUsers();
	}

	@Override
	public UpdateUserRequest createOrUpdate(UpdateUserRequest request) {
		Users user = null;
		UpdateUserRequest result = null;
		if (request.getUserId() != null ) {
			user = usersRepository.findByUserId(request.getUserId());
			user.setFirstname(request.getFirstname());
			user.setLastname(request.getLastname());
			user.setActive(request.getActive());
			user.setEmail(request.getEmail());
			user.setAddress(request.getAddress());
			
			user = usersRepository.save(user);
			result = mapper.map(user, UpdateUserRequest.class);
		} else {
			SignupRequest sigupRequest = new SignupRequest();
			sigupRequest.setAddress(request.getEmail());
			sigupRequest.setEmail(request.getEmail());
			sigupRequest.setPassword(encoder.encode(Constants.DEFAULT_PASSWORD));
			sigupRequest.setPhone(request.getPhone());
			sigupRequest.setUserName(request.getUserName());
			sigupRequest.setFirstName(request.getFirstname());
			sigupRequest.setLastName(request.getLastname());
			sigupRequest.setRoles("landlord");
			signUp(sigupRequest);
		}
		
		return result;
	}
	
	
}
