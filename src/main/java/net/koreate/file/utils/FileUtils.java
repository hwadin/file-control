package net.koreate.file.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	//파일업로드하려면 파일정보와 업로드할 파일위치필요함
	public static String uploadFile(
			String realPath,
			MultipartFile file
		)throws Exception{
	String uploadFileName = "";
	
	UUID uid = UUID.randomUUID();
	String originalName = file.getOriginalFilename();
	String savedName = uid.toString().replace("-", "");
	// dsfsdf7df66fd767f8ds6f_박주신_그날.jpg
	// dsfsdf7df66fd767f8ds6f_박주신 그날.jpg
	savedName += "_"+(originalName.replace("_"," ")); 
	System.out.println(savedName);
	// \2022\08\04
	String datePath = calcPath(realPath);
	File f = new File(realPath+datePath, savedName);
	file.transferTo(f); // 여기서 이미 파일 저장됨
	
	// 업로드된 파일의 확장자
	String formatName  // AOP.jpg 3 + 1 = 4 substring(4) => jpg
	= originalName.substring(originalName.lastIndexOf(".")+1);
	System.out.println(formatName);
	if(MediaUtils.getMediaType(formatName) != null) {
		// 이미지 파일 
		uploadFileName = makeThumbnail(realPath,datePath,savedName);
	}else {
		// 일반 파일 
		uploadFileName = makeFileName(realPath,datePath,savedName);
	}
	System.out.println("uploadFileName :"+uploadFileName );
	return uploadFileName;
}
	// URL 경로로 변경하여 반환
	private static String makeFileName(String realPath, String datePath, String savedName) {
		String fileName = "";
		fileName = datePath+File.separator+savedName;
		fileName = fileName.replace(File.separatorChar, '/');		
		return fileName;
	}

	// 썸네일 생성 후 URL 경로로 썸네일 이미지 경로 반환
	private static String makeThumbnail(String realPath, String datePath, String savedName) throws IOException {
		String name = "";
		// 썸네일 이미지 생성
		File file = new File(realPath+datePath,savedName); 
		// 지정된 위치의 이미지 정보를 BufferedImage 타입으로 반환
		BufferedImage image = ImageIO.read(file);
		
		// scalr 객체를 이용해서 원본 이미지 복제
		// 복제시 크기 지정
		BufferedImage sourceImage
		= Scalr.resize(image, 
				Scalr.Method.AUTOMATIC,	//비율에 맞춰 크기를 자동으로 지정	
				Scalr.Mode.FIT_TO_HEIGHT, // 높이를 고정 크기로 지정
				100);					// 높이는 100px, 너비는 비율에 따라 자동으로 조절
		
		// 원본이미지와 썸네일 이미지 이름 다르게 지정
		String thumbnailImage
		= realPath+datePath+File.separator+"s_"+savedName;
		//확장자 명 들고옴
		String ext = savedName.substring(
				savedName.lastIndexOf(".")+1
				);
		ImageIO.write(sourceImage, ext, new File(thumbnailImage));
		
		// url경로로 인식하기 위해  \2022\08\04  \ -> / 로 변경
		
		name = thumbnailImage.substring(realPath.length())
				.replace(File.separatorChar, '/');
		System.out.println(name);
		return name;
	}


	public static String calcPath(String realPath) {
		String datePath = "";
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator+cal.get(Calendar.YEAR);
		// /2022
		
		String monthPath = yearPath + File.separator
					   + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
		// /2022 + /08
		datePath = monthPath +File.separator
					   + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		// /2022/08 + /04
		System.out.println(datePath);
		mkDir(realPath,yearPath,monthPath,datePath);
		return datePath;
	}
	
	// 날짜 형식의 디렉토리 생성 							가변 인자
	public static void mkDir(String realPath, String... path) {
		// 오늘 날짜로 된 파일이 존재하면
		if(new File(realPath+path[path.length-1]).exists()) {
			return;
		}
		// p : 연 , 월 , 일 차례대로 꺼내옴
		for(String p : path) {
			String mkDir = realPath+p;
			System.out.println("mkDir : " + mkDir);
			File file = new File(mkDir);
			if(!file.exists()) {
				file.mkdir();
		}
	}
}
	public static boolean deleteFile(
			String realPath,
			String fileName
			) throws Exception{
		boolean isDeleted = false;
		// 일반 파일 -> 파일 삭제
		// 이미지 파일 -> 원본,썸네일 삭제
//		/2022/08/04/s_d022bb1b63b345838e58ccbf4732251b_puppy.jpg		
		String formatName = 
		fileName.substring(fileName.lastIndexOf(".")+1);
		fileName = (fileName).replace('/', File.separatorChar);
		// 일반 파일이나, 썸네일 이미지 삭제
		isDeleted = new File(realPath+(fileName)).delete();
		
		if(MediaUtils.getMediaType(formatName) != null) {
			// 이미지 파일
			// 이미지 원본 파일 도 삭제
			fileName = fileName.replace("s_", "");
			isDeleted = new File(realPath+fileName).delete();
		}
		return isDeleted;
	}

}
