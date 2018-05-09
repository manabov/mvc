package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class ReplyReg implements Action {
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		// replyForm에서 보낸 파라미터를 저정할 VO 객체를 하나 새로 만든다.
		BoardVO vo = new BoardVO();
		vo.setId(Integer.parseInt(request.getParameter("id")));
		vo.setTitle(request.getParameter("title"));
		vo.setPname(request.getParameter("pname"));
		vo.setPw(request.getParameter("pw"));
		vo.setContent(request.getParameter("content"));
		
		// DAO의 reply() 호출
		int id = new BoardDAO().reply(vo);
		
		ActionData data = new ActionData();
		data.setRedirect(true);
		data.setPath("Detail?id="+id+"&page="+request.getParameter("page"));
		
		return data;
	}
}