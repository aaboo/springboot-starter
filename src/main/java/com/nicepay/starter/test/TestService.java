package com.nicepay.starter.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nicepay.starter.common.Response;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TestService {

	@Autowired Gson gson;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ResponseEntity<String> home(HttpServletRequest req){
		return Response.msg("home 성공");
	}
	public ResponseEntity<String> test1(HttpServletRequest req){
		return Response.msg("test1 성공");
	}
	public ResponseEntity<String> test2(HttpServletRequest req){
		return Response.msg("test2 성공");
	}
}
