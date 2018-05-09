package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;

public class List implements Action{
	
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		int page = 1; // 일단 page의 기본값으로 1을 준다. 가장 첫번째로 실행되는 순간을 위하여 값지정이 필요하다.
		int limit = 3;  // 한 페이지 당 최대 글수를 설정한다. 
		int pageLimit = 4; // 한 페이지 당 최대 페이지 번호 개수를 설정한다.
		
		// 만약 page 정보를 파라미터로 받아왔다면? (맨 처음 빼고는 항상 이 if문에 걸려야만 한다.(그래야 끊임없이 page를 물고 넘나들 수 있다)
		if(request.getParameter("page") != null && !request.getParameter("page").equals("")) { 
			page = Integer.parseInt(request.getParameter("page")); // page를 그 파라미터 값으로 바꿔준다.
		} // 아니라면 page는 그냥 1일 것이고(InsertReg의 경우를 생각해보라), 별도로 받아온 page값이 있다면 page는 그 값이 될 것이다. 
		
		//예를 들어 page가 6이었다고 가정해보자.
		
		int start = (page-1) * limit + 1; // 현 페이지 (6) 에서 첫번째 글의 번호를 구하는 공식 --> 16
		int end = page * limit; // 현재 페이지(6)에서 마지막 글 번호 구하는 공식 --> 18		
		
		BoardDAO dao = new BoardDAO();
		
		
		
		
		
		int total = dao.totalCount(); // 레코드의 총 개수를 갖고 온다 --> 현재 43개??
		int totalPage = total/limit;  // 해당 게시판에 필요한 총 페이지 수는 레코드의 총 개수(total)와 limit에 의해 결정된다. 
		
		if(total % limit != 0) {     // total/limit의 나머지가 있을 경우에는 totalPage에 1을 더해줘야 한다.
			totalPage++;             // 15
		}
		
		int startPage = ((page-1) / pageLimit) * pageLimit + 1;  // 현재 페이지 하단의 첫번째 페이지번호 -- 5   
		int endPage = startPage + pageLimit - 1;                 // 현재 페이지 하단의 마지막 페이지번호 -- 8
		
		if(endPage > totalPage) {    // 한 페이지 하단의 마지막 페이지 번호(endPage)가 실제로 필요한 총 페이지수보다 큰 경우에는
			endPage = totalPage;     // endPage를 실제로 필요한 페이지 수로 설정한다.
		}
		
		// list.jsp에서 사용할 것들을 가져간다.
		request.setAttribute("page", page); // list.jsp에서는 ${param.page}가 아니라 ${page}로 바로 쓴다.
		request.setAttribute("start", start); // 게시글 번호		
		request.setAttribute("startPage", startPage); // 하단의 페이지 넘버 
		request.setAttribute("endPage", endPage); // 하단의 페이지 넘버
		request.setAttribute("totalPage", totalPage); // 하단의 페이지 넘버
		request.setAttribute("main", "list.jsp");
		request.setAttribute("data", dao.list(start, end));
		
		return new ActionData();
	}
}
