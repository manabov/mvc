<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<form action="DeleteReg" method="post">
<!-- 일단 hidden으로!!! parameter로 보내기 위해서!  -->
<input type="hidden" name="id" value="${param.id }"/>
<input type="hidden" name="page" value="${param.page}">

<table border="">
	<tr>
		<td>암호입력</td>
		<td><input type="text" name="pw"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" value="삭제하기">
			<a href="Detail?id=${param.id }&page=${param.page}">뒤로</a>
		</td>
	</tr>
</table>
</form>
