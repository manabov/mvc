package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Action;
import model.ActionData;

public class InsertForm implements Action{
	
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		ActionData res = new ActionData();
		
		// 얘는 그냥 form view로 이동시키기만 하는 역할.
		request.setAttribute("main", "insertForm.jsp");
		
		return res;
	}
}