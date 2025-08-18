package com.nicepay.starter.common;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class Utils {

	private static Environment environment; 
	
	//application.${spring.profiles.active}.properties 정보 가져오기
	@Autowired private void setEnvironment(Environment environment) {
		Utils.environment = environment;
	}
	
	//application.properties 정보 가져오기
	public static String getProperty(String propertyKey) {
		String result = environment.getProperty(propertyKey);
		if(result==null) result = "";
		return result;
	}
	public static String getProperty(String propertyKey, Object defaultValue) {
		String result = environment.getProperty(propertyKey);
		if(result==null) result = String.valueOf(defaultValue);
		return result;
	}
//	//sha256변환 : login()[//로그인 확인]에 사용3
//	public static String getSHA256(String str){
//		String SHA = ""; 
//		try{
//			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
//			sh.update(str.getBytes()); 
//			byte byteData[] = sh.digest();
//			StringBuffer sb = new StringBuffer(); 
//			for(int i = 0 ; i < byteData.length ; i++){
//				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
//			}
//			SHA = sb.toString();
//		}catch(NoSuchAlgorithmException e){
//			e.printStackTrace(); 
//			SHA = null; 
//		}
//		return SHA;
//	}
	
	
	public static String getSHA256(String input){
		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}

	public static String getSHA512(String input){
		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}	

	
	//조회 자료 건수 제한
	public static String pageLimitCheckMsg(String mode, int totalCount) {		
		String message = "";
		String pageLimit = "0";
		if (mode.equalsIgnoreCase("EXCEL")) { // 엑셀
			pageLimit = getProperty("excel.limit", 100);
			message = pageLimit + "건 초과 자료는 본사로 자료를 요청해 주세요.(총 " + totalCount + "건)";
		}else{//페이징 및 출력
			pageLimit = getProperty("page.limit", 100);
			message = pageLimit + "건 초과 자료는 엑셀을 다운받아 사용해 주세요.(총 " + totalCount + "건)";
		}
		return message;
	}
}
