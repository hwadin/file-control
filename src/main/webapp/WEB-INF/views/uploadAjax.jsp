<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadAjax.jsp</title>
<style>
	.profile_img_wrap .img_cover > label{
		background-image:url('${path}/resources/img/camera.png');
		background-size:30px;
		background-repeat:no-repeat;
		width:30px;
		height:30px;
		display:inline-block;
		margin:0;
	}
	.profile_img_wrap .profile_img{
	width:128px;
	height:128px;
	border-radius:64px;
	}
	
	.profile_img_wrap .trash_cover{
	width:40px;
	height:40px;
	position:absolute;
	bottom:5px;
	z-index:1000; /*사용자에게 가까울지,화면에가까울지?  */
	border:1px solid white;
	background-color:#aaaaaa99;
	border-radius:20px;
	padding:4px;
	box-sizing:border-box; /*외곽선크기상관없이  박스크기지정 */
	}
	
	.profile_img_wrap .trash_cover > label{
	display: block;
	background-image:url('${path}/resources/img/trash.png');
	background-size:30px;
	background-repeat:no-repeat;
	width:30px;
	height:30px;
	margin:0;
	}
	
	.profile_img_wrap{
	position:relative; /* 부모도 3차원특성 가질수 있게  */
	margin:20px auto;
	width:128px;
	}
	
	.profile_img_wrap .img_cover{
	width:40px;
	height:40px;
	position:absolute;
	bottom:5px;
	right:5px;
	z-index:1000;
	border-radius:20px;
	background-color:#aaaaaa99;
	border:1px solid white;
	box-sizing:border-box;
	padding:4px;
	}	
	
	.profile_img_wrap .img_cover .img_file,
	#delete_img{
	display:none;
	}
	
	.fileDrop{
		width:100%;
		height:200px;
		background-color:#cccccc;
		border:1px solid skyblue;
	}		
	
</style>
</head>
<body>
	<h1>Upload Ajax</h1>
	
		<div class="profile_img_wrap">
			<img id="profile_img" class="profile_img" src="${path}/resources/img/profile.jpg"/>
		
			<div class="trash_cover" id="delete_img">
				<label></label>
			</div>	
		
			<div class="img_cover">
				<label for="img_file"></label> <!--아이디값이 img_file이되서,밑에태그랑같이인식  -->
				<input type="file" class="img_file" 
					onchange="profileUpload(this.files);"
					id="img_file" accept=".gif, .jpg, .png"/>
				<input type="hidden" id="uimage"/>	
			</div>
		</div>
	
	<br/><hr/><br/>
	
	<h2>file drag &amp; drop</h2>
	<div class="fileDrop"></div>
	<div id="uploadedList"></div>
	
	<br/><hr/><br/>
	
	<form action="uploadForm" method="POST" enctype="multipart/form-data">
		<div>
			<img src="${path}/resources/img/profile.jpg" id="sampleImage" /> <!-- 미리보기  -->
			<br/>
			<input type="file" name="file" id="profileImage" accept="image/*"/>
			<input type="submit" />
		</div>
	</form>
	

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>		 /* value값이 변경되었을때 기능 실행 */
$("#profileImage").on("change",function(){
	var files = this.files; // file에 대한 정보를 배열로 나타냄.
	console.log(files);
	var file = files[0];
	console.log(file);
	// createObjectURL : 사용자 컴퓨터에서 실제 파일 이 저장된 위치 정보를 문자열로 반환
	var path = window.URL.createObjectURL(file);
	console.log(path);
	$("#sampleImage").attr("src",path); //이미지경로를 path경로로 변경
});


	var profile = "";
	
	/* 업로드 해야할 파일정보  */
	function profileUpload(files){
		console.log(files);
		// ajax로 폼데이터(입력폼) 형태로 전송을 가능하게 해주는 객체
		var formData = new FormData();
		formData.append("file",files[0]); // 데이터를 배열형태로 저장
		
		$.ajax({
			type : "POST",
			url : "uploadAjax",
			data : formData, 
			contentType : false,//인코딩이된상태에서 전달되기때문에 false로 지정
			processData : false, //쿼리스트링 생성하지않고 일반 데이터 전달
			dataType : "text", //이미지정보를 문자열로 전달
			success : function(result){
				//alert(result);
				// result = /2022/08/04/fcbhjsdf8sdf8s9d7f87_s_sdjlf.jpg
				profile = $("#profile_img").attr("src");
				$("#profile_img").attr("src","${path}/upload"+result);
				$("#uimage").val(result);
				$("#delete_img").fadeIn("fast");
			}
		});
		
	}
	// 파일 정보 삭제
	$("#delete_img").click(function(){
		var fileName = $("#uimage").val();
		$.ajax({
			url : "deleteFile",
			type : "DELETE",
			data : {fileName : fileName},
			dataType : "text",
			success : function(result){
				console.log("SUCCESS " + result);
				alert(result);
				$("#profile_img").attr("src",profile);
				$("#delete_img").fadeOut("fast");
			},
			error : function(res){
				console.log("FAILED " + result);
				alert(res.responseText);
			}
		});
	});
	
	// drag & drop
	$(".fileDrop").on("dragenter dragover",function(event){
		event.preventDefault(); //이벤트 작동 x
	});
	
	$(".fileDrop").on("drop",function(event){
		event.preventDefault();
		// alert("drop!!!");
		// 이벤트(드랍했을때)에서 파일을 가지고옴
		var files = event.originalEvent.dataTransfer.files;
		console.log(files); 
		// 한번에 여러개의 파일 저장할수도 있기때문에 배열에 저장
		var formData = new FormData();
		
		for(var i=0; i<files.length; i++){
			var file = files[i];
			console.log(file);
			var maxSize = 10475760; //10메가바이트
			if(maxSize < file.size){
				alert("업로드 할 수 없는 크기의 파일입니다.");
				return;
			}
			formData.append("files",file);
		}

		$.ajax({
			type : "POST",
			url : "uploadFiles",
			data : formData,
			dataType : "json", // 파일이름을 리스트로 받기위해
			processData : false,
			contentType : false,
			success : function(result){
				// List<String> saves == result
				console.log(result);
				var str = "";
				$(result).each(function(){
					console.log(this);
					str += "<div>";
					if(checkImageType(this)){
						console.log("이미지 파일");										//새창에 열기
						str += "<a href='${path}/upload"+getOiginImage(this)+"'  target='_blank'>";
						str += "<img src='${path}/upload"+this+"' />";
						str += "</a>";
					}else{
						console.log("일반 파일");
						str += "<a href='${path}/upload"+this+"'>";
						str += "<img src='${path}/resources/img/file.png' />";
						str += getOriginalName(this);
						str += "</a>";
					}
								//data- 사용자정의형
					str += "&nbsp;&nbsp;&nbsp;<span data-giguen='"+this+"'>&times;</span>";
					str += "</div>";
				}); // end each
				$("#uploadedList").append(str);
				
			}
		});
		
	});
	
	// 원본 이미지 파일 이름 -> 썸네일이미지 누르면 a링크로 원본파일 전달
	function getOiginImage(fileName){
		return fileName.replace("s_","");	
	}
	
	// x 눌렀을때 콘솔창에 이벤트 발생 , 파일 삭제
	$("#uploadedList").on("click","span",function(){
	var target = $(this);
	var fileName = target.attr("data-giguen");
	console.log(fileName);
	$.ajax({
		type : "DELETE",
		url : "deleteFile",
		dataType : "text",
		data : {fileName : fileName},
		success : function(result){
			if(data = "DELETED"){
				alert("삭제 완료");
				target.parent("div").remove(); //target:span
			}
		}
	});	
});
	// 파일 사진 옆에 원본 파일 이름 출력
	function getOriginalName(fileName){
		
		var index = fileName.indexOf("_")+1;
		return fileName.substr(index);		
	}
	
	function checkImageType(fileName){
		// 정규 표현식 - 특정 패턴의 문자열을 찾기 위한 표현 방식
		// Regular expression - 규칙적인 패턴 
		// | =or /  i = 이문자중에 하나라도 들어가있으면 패턴을 만족한다
		var pattern = /jpg|jpeg|gif|png/i;
		var result = fileName.match(pattern); //일치하지않으면 null반환
		console.log(result);
		return result;
		
		
	}
	
</script>
</body>
</html>