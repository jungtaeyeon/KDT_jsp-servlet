package com.example.demo.pojo3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

// if문을 사용해서 직관적이지 못했던 부분을 따로 떼어낸다.
// 왜냐하면 복잡하다고 생각하니까. -> 초보개발자들
// 복잡한 건 숨겨주자 -> 안보이는곳에서 해주자 -> 몰래 해준다. -> 프레임워크 역할 -> 제어권은 내가 갖자(IoC-제어역전) -> 못믿겠으니까
// 주니어 시니어 평준화
// if문을 클래스로 설계해 낼 수 있도록 훈련하자 - 자바기초
// 나도 서블릿과 의존관계에 있다. 왜? -> req, res를 메소드의 파라미터로 받아야 하니까
public class HandlerMapping {
	static Logger logger = Logger.getLogger(HandlerMapping.class);
	// pojo2와 달라진 부분
	// 1) 직관적이지 않았던 if문을 전담하는 클래스를 따로 구성하였다. - 이런 클래스를 프레임워크가 지원하는 것 이다.
	// ㄴ> F/W설계하는 사람이 정한 약속 - 컨벤션이다.
	// 2) 컨트롤 클래스에서 부터 메소드를 정의해 본다. - 반드시 req와 res를 지원하는 메소드이어야 한다. -> 중요
	/**********************************************************************************
	 * 
	 * @param upmu(upmu[0] - workename: 어떤 컨트롤클래스를 주입 받을지를 결정하는데 사용함
	 * @param upmu(upmu[1] - 메소드이름, 화면의 이름 
	 * @param req - 저장소, 사용자가 입력한 요청을 듣기, forward, session = req.getSession();
	 * @param res - 마임타입을 정함(jsp가 json일 수 있다.)
	 * @return - String/int/ModelAndView(select 일 때-UI를 위한 클래서 설계)
	 **********************************************************************************/
	// 사용자 정의 메소드
	public static Object getController(String[] upmu, HttpServletRequest req, HttpServletResponse res) {
		Controller3 controller = null;
		Object obj = null;
		ModelAndView mav = null;
		if("auth/kakao/callback".equals(upmu[0])) { // 카카오 로그인
			logger.info("카카오 로그인");
			controller = new KakaoController();
		}
		
		///////////////////////// [[ 게시판  시작 ]] /////////////////////////
		else if("board2".equals(upmu[0])) {
			controller = new Board2Controller();
			if("boardList".equals(upmu[1])) {
				obj = controller.boardList(req, res);
				if(obj instanceof ModelAndView) {
					return (ModelAndView)obj;
				}
				else if (obj instanceof String) {
					return (String)obj;
				}
			} /////////// end of boardList메소드 호출
			else if("boardDetail".equals(upmu[1])) {
				obj = controller.boardDetail(req, res);
				if(obj instanceof ModelAndView) {
					return (ModelAndView)obj;
				}
				else if (obj instanceof String) {
					return (String)obj;
				}
			} /////////// end of boardDetail메소드 호출
			else if("jsonBoardList".equals(upmu[1])) {
				obj = controller.jsonBoardList(req, res);
				if(obj instanceof ModelAndView) {
					return (ModelAndView)obj;
				}
				else if (obj instanceof String) {
					return (String)obj;
				}
			} /////////// end of jsonBoardList메소드 호출
			else if("boardInset".equals(upmu[1])) {
				obj = controller.boardInset(req, res);
				if (obj instanceof String) {
					return (String)obj;
				}
			} /////////// end of boardInset메소드 호출
			else if("boardUpdate".equals(upmu[1])) {
				obj = controller.boardUpdate(req, res);
				if (obj instanceof String) {
					return (String)obj;
				}
			} /////////// end of boardUpdate메소드 호출
			else if("boardDelete".equals(upmu[1])) {
				obj = controller.boardDelete(req, res);
				if (obj instanceof String) {
					return (String)obj;
				}
			} /////////// end of boardDelete메소드 호출
		}
		///////////////////////// [[ 게시판    끝 ]] /////////////////////////
		///////////////////////// [[ 공지사항 시작 ]] /////////////////////////
		else if("notice".equals(upmu[0])) {
//			controller = new NoticeController();
		}
		///////////////////////// [[ 공지사항  끝 ]] /////////////////////////
		return obj;
	}
}
