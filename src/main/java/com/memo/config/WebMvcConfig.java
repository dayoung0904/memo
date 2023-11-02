package com.memo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;

@Configuration // 설정을 위한 spring bean
public class WebMvcConfig implements WebMvcConfigurer {

	// 웹 이미지 path와 서버에 업로드 된 이미지와 매핑 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/images/**") // web image path http://localhost:8080/images/aaaa_현재시간/sun.png
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // 실제 파일 위치 // 경로 슬래쉬 갯수 : 윈도우 3개, 맥 2개
	}
}
