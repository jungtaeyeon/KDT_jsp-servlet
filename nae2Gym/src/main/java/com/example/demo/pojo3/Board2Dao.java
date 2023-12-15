package com.example.demo.pojo3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.util.MyBatisCommonFactory;

public class Board2Dao {
	Logger logger = Logger.getLogger(Board2Dao.class);
	SqlSessionFactory sqlSessionFactory = null;
	
	public Board2Dao() {
		sqlSessionFactory = MyBatisCommonFactory.getSqlSessionFactory();
	}

	public List<Map<String, Object>> boardList(Map<String, Object> pMap) { 
		logger.info("boardList"); 
		List<Map<String,Object>> bList = new ArrayList<>();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {  
			bList = sqlSession.selectList("boardList", pMap);
			logger.info(bList.toString());
		} catch (Exception e) {
			logger.info(e.toString());
		}
		return bList;
	}

	public int boardInsert(Map<String, Object> pMap) {
		logger.info("boarInsert");
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			result = sqlSession.insert("boardInsert", pMap);			
			sqlSession.commit();//빼먹으면 물리적인테이블 반영안됨
		} catch (Exception e) {
			logger.info(e.toString());
		}
		return result;
	}

	public int boardUpdate(Map<String, Object> pMap) {
		logger.info("boardUpdate");
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			result = sqlSession.update("boardUpdate", pMap);			
			sqlSession.commit();//빼먹으면 물리적인테이블 반영안됨
		} catch (Exception e) {
			logger.info(e.toString());
		}
		return result;
	}

	public int boardDelete(Map<String, Object> pMap) {
		logger.info("boardDelete");
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// board.xml에서 parameterType="int" 로 바꿨기 때문에 !
			int b_no = Integer.parseInt(pMap.get("b_no").toString());
			result = sqlSession.delete("boardDelete", b_no);	
			logger.info(result);
			
//			result = sqlSession.delete("boardDelete", pMap);			
			sqlSession.commit();//빼먹으면 물리적인테이블 반영안됨
		} catch (Exception e) {
			logger.info(e.toString());
		}
		return result;
	}
}
