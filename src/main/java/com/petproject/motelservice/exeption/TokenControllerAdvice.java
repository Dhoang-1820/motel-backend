package com.petproject.motelservice.exeption;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {

	
	 @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ErrorMessage handlebadCredentialException(UsernameNotFoundException ex, WebRequest request) {
		System.out.println("Unauthorized error: " + ex.getMessage());
		return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(),
				request.getDescription(false));
	}
}
