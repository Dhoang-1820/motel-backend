package com.petproject.motelservice.security.jwt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
			AuthenticationException exception) throws IOException, ServletException {
		Map<String, Object> response = new HashMap<>();
		response.put("status", "34");
		response.put("message", "unauthorized access");

		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = httpServletResponse.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
		out.flush();

	}

}
