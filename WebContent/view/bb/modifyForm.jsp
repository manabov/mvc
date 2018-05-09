<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<script>

<c:if test="${msg != null }">
alert("${msg }");
</c:if>

function fileDelete() {
	
	if(confirm('파일을 삭제하시겠습니까?\n삭제된 파일은 복구할 수 없습니다.')) {
		var frm = document.frm;
		frm.action = "FileDelete";
		
		frm.submit();
	}
}
</script>

<form name="frm" action="ModifyReg" method="post" enctype="multipart/form-data">

<input type="hidden" name="id" value="${data.id }"/>
<input type="hidden" name="seq" value="${data.seq }"/>
<input type="hidden" name="page" value="${param.page }"/>

	<table border="">
		<tr>
			<td>제목</td>
			<td><input type="text" name="title" value="${data.title}"></td>		
		</tr>
		<tr>
			<td>작성자</td>
			<td><input type="text" name="pname" value="${data.pname}"></td>		
		</tr>
		<tr>
			<td>암호</td>
			<td><input type="text" name="pw"></td>
		</tr>
		
	<c:choose>
	<c:when test = "${data.seq == 0 }">
	
		<tr>
			<td>파일</td>
			<td>
			<c:choose>
				<c:when test="${data.upfile != ''}">   <!-- 기존의 파일이 존재하는 경우 -->
					${data.upfile}   
					<!-- 기존의 파일을 삭제하지 않고 그대로 올리는 경우-->
					<input type="hidden" name="upfile" value="${data.upfile }">
					
					<!-- 이 버튼을 누르면 form에 있는 정보를 다 갖고간다 -->
					<input type="button" value="파일삭제" onclick="fileDelete()">
				</c:when>
				
				<c:otherwise> <!-- 기존에 파일이 없던 경우 -->
					<input type="file" name="upfile"/>
				</c:otherwise>
			</c:choose>
			</td>			
		</tr>
	</c:when>
	
	<c:otherwise>
		<input type="hidden" name="upfile" value="">
	</c:otherwise>
	
	</c:choose>	
		<tr>
			<td>내용</td>
			<td><textarea name = "content" cols="30" rows="5">${data.content }</textarea></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="수정하기">
				<!-- <input type="reset" value="초기화"> 
				
				초기화를 누르면 원래의 Modify형태로 돌아가도록 하기 위해서 아래처럼 해줘야 함.. -->
				<a href="ModifyForm?page=${param.page }&id=${data.id }">초기화</a>
				<a href="Detail?page=${param.page }&id=${data.id }">뒤로 가기</a>
				<a href="List?page=${param.page }">목록으로</a>
			</td>
		</tr>
	</table>
</form>
