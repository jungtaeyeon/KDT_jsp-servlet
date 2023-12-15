package com.example.demo.pojo3;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.example.demo.pojo2.BoardController;
import com.example.demo.pojo2.Controller;

// url을 통해서 요청을 받으므로 그 값을 이용하여 메소드 이름을 결정 한다. - upmu.length=2
@SuppressWarnings("serial")
@WebServlet("*.gd3")
public class ActionSupport extends HttpServlet {
	Logger logger = Logger.getLogger(ActionSupport.class);
	
	private void doService(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		String  uri = req.getRequestURI(); // => /notice/noticeInsert.gd?n_title=a&n_content=b
		logger.info(uri);
		String context = req.getContextPath();// /
		logger.info(context);
		String command = uri.substring(context.length()+1);//-> notice/noticeInsert.gd
		logger.info(command);
		//뒤에 의미없는 확장자 gd를 잘라내기
		int end = command.lastIndexOf(".");//점이 있는 위치정보를 가져온다
		logger.info(""+end);
		command =  command.substring(0,end);//-> notice/noticeInsert까지만 가져온다. .gd는 빼고서....
		logger.info(command);//-> notice/noticeList or notice/noticeInsert or notice/noticeUpdate or notice/noticeDelete
		String upmu[] = null;
		String result = null; // -> redirect:/board/boardInsert, forward:/board/boardList
		upmu = command.split("/");
		for(String name:upmu) {
			// 단, 반드시 추상메소드를 먼저 설계할 것 !!!!! -> 이 약속이 지켜져야 컴파일 에러가 없다.
			logger.info(name); // upmu[0] = board2  upmu[1] = boardList.jsp, boardList.gd3, boardInsert.gd3 ...
		}
		// 여기서 getController메소드를 호출
		Object obj = null;
		try {
			// 왜 이자리에 이 코드를 넣는지 생각해보자 -> 이 요청을 어떤 컨트롤러 클래스가 담당하나요??
			obj = HandlerMapping.getController(upmu, req, res); // upmu[0] = board2
		} catch (Exception e) {
			logger.info(e.toString());
		}
		// ViewResolver와 관련된 코드 시작
		// getController가 호출된 다음에 반드시 !!!!
		// 리턴타입을 받아서 타입체크를 하고 String일 때 ModelAndView일 때를 나누어 처리해야 하니까!
		// NullPointerException이 발동할 가능성이 있는 섹션이다.
		// 아래 블록이 ViewResolver안에 들어가게 됨
		if(obj != null) {
			logger.info(obj);
			String pageMove[] = null;
			ModelAndView mav = null; // 화면 처리를 위해서
			// path라면 :이 너 안에 포함되어 있어?? 네 -> redirect | forward
			// :이 없는 경우 -> WEB-INF/jsp/xxxx.jsp
			if(obj instanceof String) { // 너 String 타입이니??? 네-> json이거나 path
				if(((String)obj).contains(":")) {
					pageMove = obj.toString().split(":");
				}
				else if(((String)obj).contains("/")) {
					pageMove = obj.toString().split("/");
				}
				// :도 없고 /도 없는 경우라면 - 단순한 문자열을 반환하는 경우 - String path = "avartar.png", json형식의 문자열 같은 경우
				else {
					logger.info(obj.toString());
					pageMove = new String[1];
					pageMove[0] = obj.toString(); // 파일 이름같은 경우 저장된다. -> 화면에서 후처리하는 용도로 사용할 수 있다.
				}
			}
			else if(obj instanceof ModelAndView) { // select 일 때
				mav = (ModelAndView)obj;
				pageMove = new String[2];
				pageMove[0] = ""; // ModelAndView에는 forward일 필요는 없을것 같다.
				pageMove[1] = mav.getViewName();
				logger.info("pageMove ===> "+pageMove[0]+", "+pageMove[1]);
			}
			else if(obj instanceof byte[]) { // 응답이 이미지(png) 일 때 - Quill Editor
				res.setContentType("image/png;utf-8");
				PrintWriter out = res.getWriter();
				out.print(obj);
				return;
			}
			//
			if(pageMove != null && pageMove.length == 2) {
				logger.info("pageMove 원소의 갯수가 2개 일 때");
				String path = pageMove[1];
				if("redirect".equals(pageMove[0])) {
					res.sendRedirect(path);
				} else if("forward".equals(pageMove[0])) {
					RequestDispatcher view = req.getRequestDispatcher(path);
					view.forward(req, res);
				} else {
//					path = pageMove[0] + "/" + pageMove[1]; // board/boardList
					RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/"+path+".jsp");
					view.forward(req, res);
				}
			} //////////////// end of if
			else if(pageMove != null && pageMove.length == 1) { // quil editor에서 이미지 선택 시 파일이름 반환
				res.setContentType("text/plain;charset=utf-8");
				PrintWriter out = res.getWriter();
				out.print(obj);
				return;
			}
			// JSON 포맷으로 반환되는 값을 출력하기 - spring4.0 @ResponseBody, spring 5.0 @RestController 역할 재현
			else {
				res.setContentType("text/plain;charset=utf-8");
				PrintWriter out = res.getWriter();
				out.print(obj);
				return;
			}
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doService(req, res);
	}


	// 쿼리스트링으로 값을 넘길 때 - 링크, header, 제한적
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doService(req, res);
	}
	
	// body에 값을 넘길 때 - 서버인터셉트를 안당한다, 무조건 서버 전달, 제한이 없다 - 바이너리 타입도 담을 수 있다(첨부파일도 가능)
	// 첨부파일을 담을때는 무조건 post방식 이여야 한다. - enctype="multipart/form-data" - 바이너리로 전달(문자+숫자의 조합) - 이미지 포함 가능
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doService(req, res);
	}
	
	// 수정
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doService(req, res);
	}
}

