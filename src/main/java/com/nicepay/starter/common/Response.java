package com.nicepay.starter.common;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;


public class Response {
	
	private static final Gson gson = new Gson();
	
	//public static final String REDIRECT_LOGOUT = "/api/logout"; // 로그아웃 처리 URL
	//public static final String REDIRECT_LOGIN = "/jsp/login.jsp"; // 로그인 화면 URL
		
	@SuppressWarnings("unused") private ResponseCode code; //상태코드
	@SuppressWarnings("unused") private String message; //메시지
	@SuppressWarnings("unused") private String redirect; //리다이렉트 경로
	@SuppressWarnings("unused") private String data; //결과텍스트		
	
	Response(){
		this.code = ResponseCode.OK;
		this.message = ResponseCode.OK.getName();		
	}
	
	//최종 응답(JSON)
	public static ResponseEntity<String> finalResponse(Response res){
		HttpHeaders httpHeaders = new HttpHeaders();		
		httpHeaders.add("Content-Type", " application/json; charset=UTF-8");
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
	}
	
	//정상
	public static ResponseEntity<String> ok() {
		Response res = new Response(); 
		return finalResponse(res);
	}
	
	public static ResponseEntity<String> ok(String data) {
		Response res = new Response();
		res.data = data;
		return finalResponse(res);
	}
	
	public static ResponseEntity<String> ok(Map<String,?> resultMap) {
		Response res = new Response();
		res.data = gson.toJson(resultMap);
		return finalResponse(res);
	}
	
	public static ResponseEntity<String> ok(List<?> resultList) {
		Response res = new Response(); 
		res.data = gson.toJson(resultList);
		return finalResponse(res);
	}

	
	//에러
	public static ResponseEntity<String> error(ResponseCode resCode) {
		Response res = new Response();
		res.code = resCode;
		res.message = resCode.getName();		
		return finalResponse(res);
	}
	
	public static ResponseEntity<String> error(ResponseCode resCode, String redirect) {
		Response res = new Response();
		res.code = resCode;
		res.message = resCode.getName();
		res.redirect = redirect;
		return finalResponse(res);
	}

	public static ResponseEntity<String> error(final Exception e) {
		Response res = new Response();
		res.code = ResponseCode.ERR_NONE_PARAM;
		res.message = e.getMessage();
		return finalResponse(res);
	}
	
	//메시지만
	public static ResponseEntity<String> msg(String message) {
		Response res = new Response();
		res.code = ResponseCode.MESSAGE; 
		res.message = message;
		return finalResponse(res);
	}
	
	// 총 건수 조회 제약사항 (엑셀, 프린트는 제외)
	public static ResponseEntity<String> pageLimitError(String mode, int totalCount){		
		Response res = new Response();
		res.code = ResponseCode.PAGE_LIMIT;
		res.message = Utils.pageLimitCheckMsg(mode, totalCount);
		return finalResponse(res);
	}
	
}

