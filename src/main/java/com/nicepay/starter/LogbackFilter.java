package com.nicepay.starter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 참고사이트 : https://ckddn9496.tistory.com/89
 * FilterReply 설명
 * DENY : 로그 이벤트 동작을 취소하며, 남아있는 Filter들에 대하여 검증 하지 않는다.
 * NEUTRAL : 남아있는 다음 Filter에게 검증을 넘긴다. 만약 남아있는 Filter가 없다면 로그 이벤트가 정상적으로 동작된다.
 * ACCEPT : 로그 이벤트를 정상적으로 동작시키며, 남아있는 Filter들에 대하여 검증하지 않는다.
 * 
 * /src/main/resources/logbackn.xml 파일에 <filter class="com.nicepay.starter.LogbackFilter"/> 작성됨
 * 
 */	

public class LogbackFilter extends Filter<ILoggingEvent>{
	
	final static String[] denyTarget = {
		"com.nicepay.starter.login.LoginMapper.selectUserCheck"
	};
	
	@Override
	public FilterReply decide(ILoggingEvent event){
		String eventMessage = event.getMessage();
		Boolean isInDeny = false;
		for(int i=0, il=denyTarget.length; i<il; i++){
			if(eventMessage.contains(denyTarget[i])){
				isInDeny = true;
				break;
			}
		}
//		if(event.getMessage().contains("selectLoginAuthKey")){
		if(isInDeny){
			return FilterReply.DENY;
		}else{
			return FilterReply.ACCEPT;
		}
	}
}
