package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;

@WebServlet("/board/*")

// 여기에 multipartconfig를 넣어줘야 하긴 하는데 심각한 문제가 발생한다.
// 모든 것들이 다 multipart의 형태로 들어와야한다라는 제약이 붙어버리기 때문이다.

@MultipartConfig(
		location = "C:\\tomcat\\temp",
		maxFileSize = 1024*5000,
		maxRequestSize = 1024*1024*100,
		fileSizeThreshold = 1024*1024*10
		)

public class F_Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public F_Controller() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 이거 Encoding 안해주면 DB에 자료 삽입할 때 한글이 깨져서 들어간다.
		request.setCharacterEncoding("utf-8"); 
		
		// getRequestURI() -->  /mvcJsp/board/List 
		// getContextPath() --> /mvcJsp		
		String service = request.getRequestURI().substring((request.getContextPath()+"/board/").length());
		// List 라는 문자열만 쏙 골라낸다.
		System.out.println("service: " + service);
		
		try {	
			//class 이름갖고 새로운 클래스를 만드는 작업
			Action action = (Action)Class.forName("ttt."+service).newInstance();
			
			// ActionData형 객체를 하나 받아온다.
			// shop으로 들어왔는데 센터로 가야된대. 센터 execute로 보내면 그 안에서 또 info, gallery으로 나눔. 
			// URL을 곧 디렉토리 주소로 활용을 하겠다는 것이 restful 기법
			ActionData data = action.execute(request, response);
			
			if(data != null) { // FileDown.java에서는 화면전환이 없으므로 ActionData를 null로 반환하는 경우가 있다!
				
				if(data.isRedirect()) {
					//redirect할 경우에는 setPath()를 해줘야한다는 것을 알 수 있음!!
					response.sendRedirect(data.getPath());
				} else { // data의 isRedirect()의 default는 false이므로 여기로 오겠지?
					request.getRequestDispatcher("../view/template.jsp").forward(request, response);
				}
			} 	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
