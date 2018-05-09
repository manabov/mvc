package model;

public class ActionData {
	// ActionData가 갖는 속성정보는 2개다.
	// 1. redirect: redirect할건지 아니면 forward를 할건지.
	// 2. path: redirect하거나 forward하거나 어쨌든 이동경로는 어디로 할건지.
	
	boolean redirect = false; 
	String path = null;
	
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}