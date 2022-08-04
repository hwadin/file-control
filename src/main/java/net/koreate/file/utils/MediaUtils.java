package net.koreate.file.utils;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

//이미지로 판별할 MIME type을 저장해놓을 객체
public class MediaUtils {
	
	private static Map<String,MediaType> mediaMap;
	
	// 클래스정보가 등록되면 즉시 실행
	static {
		mediaMap = new HashMap<>();
		// JPG, image/jpeg
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
		mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
	}
	
	//확장자 명을 전달 받아 동일한 이름의 MediaType이 존재하는지
	//확인 후 반환
	public static MediaType getMediaType(String ext) {
		// 대문자로 변경 후 키값으로 전달 
		return mediaMap.get(ext.toUpperCase());
		
	}
}
