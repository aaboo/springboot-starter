package com.nicepay.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Thyemeleaf Template HTML에서 static 폴더에 있는 라이브러리 소스를 불러올 때 아래 설정이 필요함 
	 * 샘플: <script type="text/javascript" src="/plugins/js/jquery.min.js"/>
	 * 실재경로 : /src/main/resources/static/plugins/js/jquery.min.js
	 * application.properties 파일에서 아래처럼 적용처리
	 * 	spring.web.resources.static-locations=classpath:/static/
	 */
	/*
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/static/");
	}
	*/
}
