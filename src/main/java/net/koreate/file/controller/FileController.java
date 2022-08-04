package net.koreate.file.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koreate.file.utils.MediaUtils;
import net.koreate.file.vo.FileVO;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileController {
	
	private final String uploadPath; // C:\\Temp\\upload
	private final String uploadFolder; // upload
	private final ServletContext context;
	
	String realPath;

	@PostConstruct
	public void initController() throws ClassNotFoundException {
		System.out.println("initController uploadPath : "+uploadPath);
		System.out.println("initController uploadFolder : "+uploadFolder);
		realPath = context.getRealPath(File.separator+uploadFolder);
		System.out.println(realPath);
		File file = new File(realPath);
		// 경로상에 폴더가 없으면 
		if(!file.exists()) {
			// 디렉토리 생성
			file.mkdirs();
			System.out.println("folder 생성 완료");
		}
		System.out.println("FileController 생성 및 사용준비 완료");
	}
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("uploadForm")
	public void uploadForm() {}
	
	@GetMapping("uploadAjax")
	public void uploadAjax() {}
	
	@PostMapping("uploadForm")
	public String uploadForm(
			@RequestParam("file") MultipartFile file,
			Model model) throws Exception {
		System.out.println("File name : "+file.getOriginalFilename());
		System.out.println("File size : "+file.getSize());
		System.out.println("File Type : "+file.getContentType());
		byte[] bytes = file.getBytes();
		String savedName= uploadFile(file.getOriginalFilename(),bytes);
		model.addAttribute("savedName",savedName);
		
		/*
		File f = new File(realPath,file.getOriginalFilename());
		FileCopyUtils.copy(bytes, f);
		/*
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(bytes);
		fos.flush();
		fos.close();
		*/
		return "uploadResult";
	}
	
	@PostMapping("uploadForm1")
	public String uploadForm1(
				MultipartFile[] files, //파일여러개라 배열형태로 받음
				Model model
			)throws Exception{
		
		List<String> saves = new ArrayList<>();
		for(MultipartFile f : files) {
			String savedName = uploadFile(
						f.getOriginalFilename(),
						f.getBytes()
					);
			saves.add(savedName);
		}
		model.addAttribute("saves",saves);
		return "uploadResult";
	}
	
	@PostMapping("uploadForm2")
	public String uploadForm2(
			MultipartHttpServletRequest request,
			Model model
			)throws Exception{
		String auth = request.getParameter("auth");
		String content = request.getParameter("content");
		MultipartFile file = request.getFile("file");
		MultipartFile file1 = request.getFile("file1");
		
		String original = file.getOriginalFilename();
		String original1 = file1.getOriginalFilename();
		String[] saves = new String[2];
		saves[0] = uploadFile(original,file.getBytes());
		saves[1] = uploadFile(original1,file1.getBytes());
		
		model.addAttribute("saves",saves);
		model.addAttribute("auth",auth);
		model.addAttribute("content",content);
		
		return "uploadResult";
	}
	
	// auth , content, file , file1
	// MultipartHttpServletRequest 사용 안해도
	// Dispatcher servlet이 type과 변수명만 지정하면 자동으로 생성해줌
	@PostMapping("uploadForm3")
	/*public String uploadForm3(
				String auth,
				String content,
				List<MultipartFile> file,
				MultipartFile file1,
				Model model
			)throws Exception{
			*/
	public String uploadForm3(FileVO vo, Model model) throws Exception{
		List<String> saves = new ArrayList<>();
		for(MultipartFile f : vo.getFile()) {
			saves.add(
				uploadFile(
					f.getOriginalFilename(),
					f.getBytes()
				)
			);
		}
		String savedName = uploadFile(
				vo.getFile1().getOriginalFilename(),
				vo.getFile1().getBytes()
			);
		model.addAttribute("savedName",savedName);
		model.addAttribute("saves",saves);
		model.addAttribute("auth",vo.getAuth());
		model.addAttribute("content",vo.getContent());
		return "uploadResult";
	}
	
	
	
	public String uploadFile(
			String original,
			byte[] fileData
			)throws IOException {
		String savedName="";
		UUID uid=UUID.randomUUID();
		// 32개의 랜덤한 문자 + 4개의 - == 36개의 문자
		System.out.println(uid);
		savedName = uid.toString().replace("-", "")+"_"+original;
		System.out.println(savedName);
		FileCopyUtils.copy(fileData, new File(realPath,savedName));
		return savedName;
	}
	
}











