package ttt;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class FileDelete implements Action {

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		BoardVO vo = new BoardVO();
		
		vo.setId(Integer.parseInt(request.getParameter("id")));
		vo.setPw(request.getParameter("pw"));
		vo.setPname(request.getParameter("pname"));
		vo.setTitle(request.getParameter("title"));
		vo.setContent(request.getParameter("content"));
		vo.setUpfile(request.getParameter("upfile"));
		// modifyForm.jsp애서 seq를 사용하여 파일 셀을 보일지 말지를 결정하므로 이게 필요함.
		vo.setSeq(Integer.parseInt(request.getParameter("seq")));
		
		
		String msg = "인증실패";
		BoardDAO dao = new BoardDAO();
		
		if(dao.search(vo) != null) {
			
			String path = request.getRealPath("up")+"\\";
			path = "C:\\Users\\85jbb\\mvcWork\\mvcJsp\\WebContent\\up\\";
			
			// BoardVO res를 별도로 갖고오지 않았으므로 걍 request로 upfile 파라미터을 찾는다.
			new File(path + vo.getUpfile()).delete();
			
			// upfile만 삭제! 나머지 data는 남겨둔다.
			dao.fileDelete(vo.getId());
			vo.setUpfile("");
			msg = "파일 삭제 성공";
		}
		
		request.setAttribute("msg", msg);
		// modifyForm.jsp로 forwarding 하도록 설정해준다.  
		request.setAttribute("main", "modifyForm.jsp");
		
		// modify니까 data에 vo를 실어서 보내줘야 한다. 그래야 alert이 떴을 때에도 최신 내용이 계속 업데이트된 상태로 보여지게 된다.
		// 아래처럼 안하면 암호인증 실패했을 때 modifyForm.jsp에 내용이 텅 비게 된다.
		request.setAttribute("data", vo);
		
		dao.close();
		return new ActionData();
	}
}
