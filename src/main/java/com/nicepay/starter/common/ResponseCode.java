package com.nicepay.starter.common;

public enum ResponseCode {
	OK("정상")	
	//아래는 에러코드
	, ERR_NONE_AUTH("권한이 없습니다.")
	, ERR_NONE_SESSION("로그인이 필요합니다.")
	, ERR_INVALID_SESSION("비정상적인 접근입니다.")
	, ERR_DUP_SESSION("다른 사용자가 사용하고 있습니다.")
	, ERR_NONE_PARAM("요청정보가 필요합니다.")
	, ERR_INVALID_PARAM("요청정보가 유효하지 않습니다.")
	, ERR_TOKN_TEST("토큰이 유효하지 않습니다.")
	, ERR_NONE_URL("요청 경로가 유효하지 않습니다.")
	, PAGE_LIMIT("조회건수가 허용범위를 초과합니다.")
	, MESSAGE("사용자 정의 메시지")
	;
	
	final private String name;
	
	public String getName() {
		return name;
	}
	
	private ResponseCode(String name) {
		this.name = name;
	}
	
}