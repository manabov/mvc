package ttt;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class InsertReg implements Action{

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		// redirect인지 forward인지를 구분해주는 ActionData를 생성해둔다.
		ActionData data = new ActionData();
		
		// 새롭게 작성된 글의 내용을 저장하여 DB로 보내는 역할을 하는 VO를 생성한다. 
		BoardVO vo = new BoardVO();
		
		// insertForm.jsp에서 form tag를 활용하여 여기로 이동시켰으니 parameter를 get할 수 있다.
		vo.setTitle(request.getParameter("title"));
		vo.setPname(request.getParameter("pname"));
		vo.setPw(request.getParameter("pw"));
		vo.setContent(request.getParameter("content"));
		
		// 그러나 '파일'의 경우는 일반적인 경우처럼 getParameter를 할 수 없고 Part를 쓰도록 한다.
		// 파일의 경우 별도로 처리하는 fileUpload 메소드 호출!
		vo.setUpfile(fileUpload(request));
		
		//글을 DB에 저장한다.
		//근데 그러자마자 목록을 보여주는 것이 아니라 자기가 방금 쓴 글의 Detail 창을 보여주도록 하기 위한 작업이 시작된다.
		//detail을 할 때에는 항상 id를 기준으로 찾으므로 방금 쓴 글의 id를 받아오도록 한다.
		int id = new BoardDAO().insert(vo);
		
		// ActionData의 setRedirect 멤버변수가 처음으로 등장!!!, 
		// F_Controller에서 redirect가 참이므로 분기된다.
		data.setRedirect(true);
		
		// 글을 쓰면 바로 detail이 작동하도록 주소설정을 아래와 같이 한다.
		data.setPath("Detail?id="+id);
		
		return data;
	}
	
	// 여기서 fileUpload를 굳이 public으로 했던 것은 다른 클래스인 ModifyReg에서 이 메소드를 사용하려고 했던 것인데
	// 결국엔 거기서도 InsertReg 객체를 만든다음에 해당 메소드를 호출했기 때문에 굳이 public 선언을 안해도 무방하긴 하다.
	// 그러나 수업시간에 썼던 거니까 또 어떻게 진행될지 모르니 일단은 public인 채로 놔두자. 
	public String fileUpload(HttpServletRequest request) {
		
		try {
			
			// name 속성이 upfile이었던 formTag의 input 속성이 있었는지를 확인한다.
			// 처음에 실수로 request.getContentType()으로 잘못 썼었다! pp.getContentType()임을 기억할 것!!!
			Part pp = request.getPart("upfile");
			
			if(pp.getContentType() != null) { // file인지 확인.
				String fileName = "";
				
				for(String hh : pp.getHeader("Content-Disposition").split(";")) {
					if(hh.trim().startsWith("filename")) {
						fileName = hh.substring(hh.indexOf("=")+1).trim().replaceAll("\"", "");
					}
				}
				System.out.println("fileName: "+fileName);
				
				// 파일이름이 새겨졌다면 save한다!
				if(!fileName.equals("")) {
					return fileSave(pp, fileName, request);
				}
			}
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		// 파일이 비어있다면 비어있는 String을 반환한다.
		return "";
	}
	
	String fileSave(Part pp, String fileName, HttpServletRequest request) {
		
		// 파일이름이 중복될 경우 어떻게 처리할지 ??
		
		int pos = fileName.lastIndexOf(".");
		String fileDo = fileName.substring(0, pos);
		String exp = fileName.substring(pos);
		
		String path = request.getRealPath("up\\");
		path = "C:\\Users\\85jbb\\mvcWork\\mvcJsp\\WebContent\\up\\";
		
		int cnt = 0;
		File file = new File(path+fileName);
		
		// 이름이 없을 때까지 찾음.
		while(file.exists()) {
			fileName = fileDo+"_"+(cnt++)+exp;
			file = new File(path + fileName); // 이게 while문 안에 있어야 조건문에서의 file이 계속 새롭게 갱신된다!!! 우후후 ㅋㅋ
		}
		
		// 파일명 중복처리 이후에 upload한다!
		try {
			pp.write(path+fileName);
			pp.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;

	}
}
