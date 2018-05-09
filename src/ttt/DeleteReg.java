package ttt;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;


public class DeleteReg implements Action{
	
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		int page = Integer.parseInt(request.getParameter("page"));
		
		BoardVO vo = new BoardVO();
		vo.setId(Integer.parseInt(request.getParameter("id")));
		vo.setPw(request.getParameter("pw"));
		
		BoardDAO dao = new BoardDAO();
		BoardVO res = dao.search(vo);
		
		String msg = "암호 인증 실패";
		String url = "DeleteForm?id="+vo.getId()+"&page="+page;
		
		if(res != null) { // id와 pw가 일치하는 게 있다는 의미.
			if(!res.getUpfile().equals("")) { // 파일을 갖고 왔으므로 파일이 있다면??
				String path = request.getRealPath("upfile")+"\\";
				path = "C:\\Users\\85jbb\\mvcWork\\mvcJsp\\WebContent\\up\\";
				new File(path+res.getUpfile()).delete(); // 파일이 있다면 파일을 지워버린다.
			}
			
			// 파일이 있든없든 DB자료는 지운다. 파일이 없는 Record도 있을 수 있기 때문이다.
			dao.delete(vo.getId()); 
			msg = "삭제됐습니다";
			url = "List?page="+page;
		}	
		
		dao.close();
					
		request.setAttribute("main", "alert.jsp");
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		
		return new ActionData();
		
	}
}
