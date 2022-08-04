<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadForm.jsp</title>
</head>
<body>
	<h1>Upload Form</h1>
	<form action="uploadForm" 
			method="POST" 
			enctype="multipart/form-data">
		<input type="file" name="file" /> <br/>
		<input type="submit"/>
	</form>
	<h2>UploadForm1 multiple</h2>
	<form action="uploadForm1" method="post" enctype="multipart/form-data">
		<input type="file" name="files" multiple/><br/>
		<input type="submit" />
	</form>
	<h2>UploadForm2 multi upload</h2>
	<form action="uploadForm2" method="post" enctype="multipart/form-data">
		<input type="text" name="auth"/><br/>
		<textarea name="content"></textarea><br/>
		<input type="file" name="file"/><br/>
		<input type="file" name="file1"/><br/>
		<input type="submit" />
	</form>
	
	<h2>UploadFrom3 multiple upload</h2>
	<form action="uploadForm3" method="post" enctype="multipart/form-data">
		<input type="text" name="auth"/><br/>
		<textarea name="content"></textarea><br/>
		<input type="file" name="file" multiple/><br/>
		<!-- .pdf	.xml	.jsp	text/plain	text/json  -->
		<input type="file" name="file1" accept="image/*"/><br/>
		<input type="submit" />
	</form>
</body>
</html>







