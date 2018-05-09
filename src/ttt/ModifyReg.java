package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class ModifyReg implements Action{
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		BoardVO vo = new BoardVO();
		vo.setId(Integer.parseInt(request.getParameter("id")));
		vo.setPw(request.getParameter("pw")); // pw가 맞아야 수정허용.

		BoardDAO dao = new BoardDAO();
		BoardVO res = dao.search(vo); // Id와 pw가 맞은지 확인하는 절차
	
		// page변수로 연산을 할 일이 없으므로 String으로 받아도 무방하다.
		String page = request.getParameter("page");
		
		String msg = "수정 실패";
		String url = "ModifyForm?id="+vo.getId()+"&page="+page;
		
		if(res != null) { // id와 pw가 일치하는 게 있단 의미.
			
			vo.setTitle(request.getParameter("title"));
			vo.setContent(request.getParameter("content"));
			vo.setPname(request.getParameter("pname"));

			// file은 복잡하니 약간의 처리를 해줘야 함.
			if(request.getParameter("upfile") != null) { // upfile로 해서 올라오면.. 즉, modifyForm.jsp에서 기존의 파일이 있었고 그걸 수정안했으면?
				vo.setUpfile(request.getParameter("upfile"));
			
			} else { // modifyForm.jsp에서 기존에 파일이 없었고 새로운 파일을 업로드했을 경우
				     // request로 들어오는 게 아니라 파일명 들어오므로 "upfile" 파라미터는 nulldl ehlsek.
				vo.setUpfile(new InsertReg().fileUpload(request));	
			}
			
			dao.modify(vo);
			msg = "수정됐습니다";
			url = "Detail?id="+vo.getId()+"&page="+page;
		}
		
		dao.close();
		
		request.setAttribute("main", "alert.jsp");
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		
		return new ActionData();
	}
}
