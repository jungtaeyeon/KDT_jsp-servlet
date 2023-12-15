package com.example.demo.pojo3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

// -> select -> List:배열-로우 <Map:여러컬럼담기> -> jsp -> 유지 -> request -> 서블릿과 의존관계가 있다.
// -> 서블릿으로부터 완전한 독립이 안되었다 -> 불만 -> spring5.0등장
// -> request와 response가 없이는 나는 웹서비스를 구현할 수 없어요...?? x

// -> 나는 어떤 클래스에서 사용되나요?? -> 응답페이지에 대한 책임은 누가 있지?? -> XXXController
// -> 응답 페이지 처리에 대한 관심사는 MVC패턴 중에서 컨트롤계층에 있다.
// java(서블릿) -> jsp 사이에서 유지문제 해결
// ModelAndView는 생성자를 통해서 요청객체를 받아온다.
// 어디서? -> 서블릿에서 받아온다. - Board2Controller
public class ModelAndView {
	// req.setAttribute("list", list);  -> 전체조회
	HttpServletRequest req = null; // 나는 어디서 주입을 받게 되나요??
	List<Map<String, Object>> list = new ArrayList<>();
	// 컨트롤 클래스에서 설정된 화면의 이름을 담을 변수 선언
	String viewName = null;
	
	public ModelAndView(HttpServletRequest req) {
		this.req = req;
	}
	// 컨트롤 클래스에서 화면 이름이 결정되면 그 값을 읽고 쓸 수 있는 메소드를 설계
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getViewName() {
		return viewName;
	}
	
	// 유지의 문제 해결
	public void addObject(String name, Object obj) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put(name, obj);
		req.setAttribute(name, obj);
		list.add(pMap);
	}
}
