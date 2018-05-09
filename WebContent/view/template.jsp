<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<table border="">
	<tr>
		<td colspan="2">
			<jsp:include page="inc/top.jsp"/>
		</td>
		
	</tr>

	<tr>
		<td>
			<jsp:include page="inc/menu.jsp"/>
		</td>
		
		<td>
			<!-- 사실 bb도 조절을 해줘야 한다 -->
			<!-- main의 주소는 ttt 패키지 내 클래스들에서  request.setAttribute()로 설정해줄 것이다 -->
			<jsp:include page="bb/${main }"/>
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<jsp:include page="inc/bottom.jsp"/>
		</td>
	</tr>

</table>
