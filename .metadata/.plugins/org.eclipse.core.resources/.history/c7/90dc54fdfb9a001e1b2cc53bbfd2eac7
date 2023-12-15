package com.example.demo.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


//@WebServlet(urlPatterns= {"/auth", "/notice"})
@WebServlet(urlPatterns= {"*.gd3"})
public class Front1Controller extends HttpServlet {
	Logger logger = Logger.getLogger(Front1Controller.class);
	private static final long serialVersionUID = 1L;
	Controller1 controller = null;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info(req.getRequestURI());
		//logger.info(req.getRequestURL().toString());//{http://localhost:8000/auth/test.do}
		Controller1 controller = null;
		String requestURI = req.getRequestURI();
		String contextPath = req.getContextPath();//
		String command = requestURI.substring(contextPath.length()+1);
		int end = command.lastIndexOf(".");
		command = command.substring(0,end);
		logger.info(command);//auth, test
		String upmu[] = command.split("/");
		logger.info(upmu[0]+", "+upmu[1]);
		Object obj = null;
		try {
			obj = HandlerMapping.getController(upmu, req, res);
		} catch (Exception e) {
			logger.info("Exception : " + e.toString());
		}
		//////////////// [[ 응답 페이지 처리 섹션 ]] ///////////////
		if(obj !=null) {
			logger.info(obj.toString());
			String pageMove[] = null;
			ModelAndView3 mav = null;
			//
			if(obj instanceof String) {
				if(((String)obj).contains(":")) {
					logger.info(":콜론이 포함되어 있어요");
					String first = obj.toString().substring(0, 1);
					if("[".equals(first)) {
						logger.info("JSON포맷인거야? ==> "+first);
						pageMove = new String[1];
						pageMove[0] = obj.toString();
						//return;
					}
					else {
						pageMove = obj.toString().split(":");						
					}
					//pageMove[0]= redirect or forward - redirect유무담기
					//pageMove[1]=board2/boardList - 페이지 이름을 담아주세요
				}else if(((String)obj).contains("/")) {
						logger.info("/가 포함되어 있어요");
						//pageMove[0]= redirect or forward - redirect유무담기
						//pageMove[1]=board2/boardList - 페이지 이름을 담아주세요
						pageMove = obj.toString().split("/");
				}else {
					logger.info(":콜론도 /도 포함되어 있지않아요");
					pageMove = new String[1];
					pageMove[0] = obj.toString();
					logger.info("콜론도 슬래쉬도 없는 경우 : pageMove[0]==>"+pageMove[0]);
				}
				for(int i=0;i<pageMove.length;i++) {
					logger.info("pageMove["+i+"] ==>"+pageMove[i]);					
				}
			}////end of String
			// Model(데이터-내안 List있다)과 View의 혼합(함께)
			else if(obj instanceof ModelAndView3) {
				mav = (ModelAndView3)obj;
				pageMove = new String[2];
				pageMove[0] = "forward";
				pageMove[1] = mav.getViewName();
				logger.info("pageMove==>"+pageMove[0]+","+pageMove[1]);				
			}
			else if(obj instanceof byte[]) {
				//logger.info("imageGet요청 일때"+new String((byte[])obj));
				logger.info("imageGet요청 일때");
				res.setContentType("image/png;utf-8");
				PrintWriter out = res.getWriter();
				out.print(obj);
				return;
			}

			if(pageMove !=null && pageMove.length ==2) {
				logger.info("pageMove원소의 갯수가 2개 일때");
				/*
				String path = pageMove[1];
				//Board2Controller에 리턴 값이 return "redirect:boardList.jsp"일때
				if("redirect".equals(pageMove[0])) {//너 select가 아니야?
					res.sendRedirect(path);
				}
				else if("forward".equals(pageMove[0])) {//난 유지하게 해줄래
					RequestDispatcher view = req.getRequestDispatcher("/"+path+".jsp");
					view.forward(req, res);
				}
				else {//redirect나 forward문자열이 없는 경우라면?
					logger.info("redirect나 forward문자열이 없는 경우라면?");
					path = pageMove[0]+"/"+pageMove[1];
					RequestDispatcher view = 
							req.getRequestDispatcher("/WEB-INF/jsp/"+path+".jsp");
					view.forward(req, res);					
				}
				*/
				new ViewResolver(req, res, pageMove);
			}////end of pageMove 출력페이지 호출 URL패턴 조립하기
			else if(pageMove !=null && pageMove.length == 1){
				logger.info("pageMove원소의 갯수가 1개 일때");
				res.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = res.getWriter();
				out.print(pageMove[0]);
			}
			//JSON포맷으로 반환되는 값 출력하기
			else {
				logger.info("리턴결과가 JSON포맷이라서 pageMove의 갯수가 2개 이상인 경우가 나옴");
				res.setCharacterEncoding("utf-8");
				 res.setContentType("text/plain;utf-8");
				 PrintWriter out = res.getWriter();
				 out.print(obj);
				 return;
			}
		}////////end of 컨트롤계층 리턴결과			
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
}
