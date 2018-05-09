package ttt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import model.Action;
import model.ActionData;

public class FileDown implements Action{
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		
		// download도 upload와 매우 유사하다. 어렵게 생각할 필요 없다. 다운로드하려 하는 파일의 기존 경로설정. 
		String path = request.getRealPath("up");
		path = "C:\\Users\\85jbb\\mvcWork\\mvcJsp\\WebContent\\up";
		
		// 다운로드할 때 특별한 view page를 사용자나 디자이너에게 보여줄 필요가 없다.
		String fileName = request.getParameter("file");
		
		// fileName이 한글일 때 혹시 fileName자체가 깨지는지 안깨지는지 확인해볼 필요가 있다.
		System.out.println("파일명: "+ fileName); 
		// --> 안 깨지는 것 확인됐다! 그러면 문제는 setHeader에 있음. 따라서 setHeader에서 encoding된 이름을 넣어줄 필요가 있다.
		
		try {
			
			// 자바 스크립트, HTML front. Web page는 그 자체로 한글을 못 읽음. (물론 요즘은 자바 스크립트이 버전업되면서 읽어주긴 하지만)
			// Servlet 등 back단위는 한글을 인식할 수 있다.
			
			// 대화상자에 '한글'이 제대로 뜨도록 해주기 위해서 encode를 해준다.
			String enFileName = URLEncoder.encode(fileName, "utf-8");
			// 대화상자를 띄운다. attachment --> 다운로드.
			
			response.setHeader("Content-Disposition", "attachment;filename="+enFileName);
			// jsp파일에서 작업을 했더라면 여기서   out.clear();    out = pageContext.pushBody();    를 해줬어야 했는데
			// 지금은 back 단위인 Servlet에서 작업을 하므로 out을 사용할 필요가 없다. 
			// 즉, Servlet에서 다운로드를 작업하면 코드가 간결해지고 좋다.
			
			// FileInputStream에서는 enFileName을 쓰지 않고 바로 fileName을 활용한다. Back 단위, Java code에서는 한글을 쉽게 인식하기 때문이다!
			
			// 이하부터는 일반적인 파일 io 활용
			FileInputStream fis = new FileInputStream(path+"\\"+fileName);
			ServletOutputStream sos = response.getOutputStream();
			
			
			byte[] buf = new byte[1024];
			
			while(fis.available() > 0) {
				int len = fis.read(buf);
				sos.write(buf, 0, len);
			}
			
			sos.close();
			fis.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// F_Controller에서 data가 null이 아닐 때에만 redirect 또는 forward를 하여 화면상의 변화를 주게 되는데
		// 다운로드할 때는 화면이 그대로 유지되어야 하므로 null을 반환하여 F_Controller상의 ActionData data가 null이 되도록 한다.
		// 오케바리!
		return null;
	}
}
