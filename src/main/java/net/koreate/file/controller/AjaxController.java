package net.koreate.file.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.koreate.file.utils.FileUtils;

@RestController
@RequiredArgsConstructor 
public class AjaxController {

	private final String uploadFolder;
	private final ServletContext context;
	
	String realPath;
	
	@PostConstruct // 빈이 등록되고 나면 서버가 구동되기전에 딱 한번 호출 
	public void initPath() {
		realPath = context.getRealPath(
				File.separator + uploadFolder
			);
			System.out.println("realPath : " + realPath);
			File file = new File(realPath);
			if(!file.exists()) {
				file.mkdirs();
				System.out.println("디렉토리 생성 완료");
			}
			System.out.println("사용 준비 완료");
	}
	
	@PostMapping("uploadAjax")
	public ResponseEntity<String> uploadAjax(
			MultipartFile file
			)throws Exception{
		String original = file.getOriginalFilename();
		System.out.println("fileName");
		/*
		file.transferTo(new File(realPath,original)); //upload폴더에 파일객체(원본파일)를 매개변수로 전달
		String path = FileUtils.calcPath(realPath);
		System.out.println("path : "+path);
		*/
		String savedName = FileUtils.uploadFile(realPath, file);
		HttpHeaders header = new HttpHeaders(); //헤더에 한글문자열 전달
		header.add("Content-Type","text/plain;charset=utf-8");
		ResponseEntity<String> entity = new ResponseEntity<>(
				savedName, // 업로드된 파일이름
				header, //헤더정보
				HttpStatus.OK //상태정보
			);
		return entity;
	}

	// 여러 파일 업로드 요청 처리
	@PostMapping("uploadFiles")
	public ResponseEntity<List<String>> uploadFiles(
			List<MultipartFile> files
			) throws Exception{
		List<String> names = new ArrayList<>();
		for(MultipartFile m : files) {
			names.add(FileUtils.uploadFile(realPath, m));
		}
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
		return new ResponseEntity<>(names,header,HttpStatus.OK);
	}
	
	
	
	// 파일 삭제 요청 처리
	@DeleteMapping("deleteFile")
	public ResponseEntity<String> deleteFile(
				String fileName
			) throws Exception{
		ResponseEntity<String> entity = null;
		boolean isDeleted = FileUtils.deleteFile(
				realPath, 
				fileName);
		if(isDeleted) {
			entity = new ResponseEntity<>("DELETED",HttpStatus.OK);
		}else {
			entity = new ResponseEntity<>("FAILED",HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
}
