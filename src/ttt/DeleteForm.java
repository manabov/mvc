package ttt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;

public class DeleteForm implements Action{
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		ActionData data = new ActionData();
		
		// 삭제할 권한 여부를 확인하는 '암호입력'하는 페이지 deleteForm.jsp로 forward
		request.setAttribute("main", "deleteForm.jsp");
		
		return data;
	}
}
