package com.petproject.motelservice.exeption;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorMessage handlebadCredentialException(UsernameNotFoundException ex, WebRequest request) {
		System.out.println("Không tìm thấy tên đăng nhập");
		return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), "Không tìm thấy tên đăng nhập",
				request.getDescription(false));
	}
	
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		System.out.println("Mật khẩu sai");
		return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), "Mật khẩu sai",
				request.getDescription(false));
	}
}
