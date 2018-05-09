package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;

public class ModifyForm implements Action {

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		BoardDAO dao = new BoardDAO();
		// reply할 때 detail을 과정 중에 사용하기 때문에 close()를 finally에 둘 수가 없다.
		// 따라서 finally를 빼고 별도로 close()가 필요한 데에서만 service에서 close()를 해주기로 한다.
		// 고로, dao 변수가 반드시 필요하게 된다.
		request.setAttribute("data", dao.detail(Integer.parseInt(request.getParameter("id"))));
		// 닫는다.
		dao.close();
		
		// modifyForm으로 호출
		request.setAttribute("main", "modifyForm.jsp");
		return new ActionData();
	}
}
