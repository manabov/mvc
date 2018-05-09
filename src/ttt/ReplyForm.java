package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class ReplyForm implements Action {
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		//reply는 완전한 새 글을 쓴다기 보다는 특정한 원글에 달리게 되는 부수적인 글이므로
		//제목과 내용에 [답변]이라는 느낌을 줄 필요가 있다.
		BoardDAO dao = new BoardDAO();
		// 댓글이 달릴 원글의 id를 통해 BoardVO를 하나 새로 받아온다.
		BoardVO vo = dao.detail(Integer.parseInt(request.getParameter("id")));
		dao.close();
		// title과 content의 경우는 form에서부터 [답글]을 넣어주는 것이 좋으므로 아래처럼한다.
		vo.setTitle("[답변]" + vo.getTitle());
		vo.setContent("[답변]" + vo.getContent());
		// data 변수에 vo를 넣는다(title, content)
		request.setAttribute("data", vo);
		request.setAttribute("main", "replyForm.jsp");
		// 사실 main에다가 insertForm이나 modifyForm을 써도 되는데 그냥 헷갈릴 수 있으니 replyForm.jsp를 새로 만든다.
		return new ActionData();
	}
}
