package com.nicepay.starter.home;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.gson.Gson;
import com.nicepay.starter.common.JtMap;
import com.nicepay.starter.common.Utils;

@Service
public class HomeService {

protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Primary로 구성된 Mapper
	@Autowired private HomeMapper homeMapper;
	
	//@Primary가 아닌 Mapper 설정
	@Autowired @Qualifier("sqlSessionMysql") private SqlSession sqlSessionMysql;
	@Autowired private HomeMapperMysql homeMapperMysql;
	
	@Autowired private Gson gson;
	
	@Autowired TransactionTemplate transactionTemplate; //트랜잭션
	@Autowired PlatformTransactionManager platformTransactionManager; //트랜잭션
	
	//오라클테스트
	public String test() {
		return homeMapper.test();
	}
	public String heartBeat() {
		return homeMapper.heartBeat();
	}
	//MYSQL 테스트
	public String testMysql() {
		homeMapperMysql = sqlSessionMysql.getMapper(HomeMapperMysql.class);
		return gson.toJson(homeMapperMysql.testMysql());
	}
	//application.properties 파일 정보 읽기 테스트
	public String testEnv() {
		return Utils.getProperty("datasource.oracle.url");
	}
	
	//롤백 테스트 1
	@Transactional(rollbackFor=Exception.class)
	public String rollbackTest1() {
		try {
			homeMapper.rollbackTest1();//정상 업데이트
			homeMapper.rollbackTest2();//강제 에러 발생
		}catch(Exception e) {
			logger.info(e.getMessage());
			throw e; //여기서 롤백 발생 :--> /resources/templates/error.html 보여짐
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//throw e 없이 롤백 발생 :--> error.html 안보여지고 homeMapper.test() 까지 실행
		}
		return homeMapper.test(); //throw e에서 끝나기 때문에 조회되지 아니함.
	}
	
	//롤백 테스트 2
	public String rollbackTest2() {
		try {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					homeMapper.rollbackTest1();//정상 업데이트
					homeMapper.rollbackTest2();//강제 에러 발생
				}
			});
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return homeMapper.test();//롤백 된 상태로 반환
	}
	
	//롤백 테스트 3 -- 가장 편한 방법
	public String rollbackTest3() {
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			homeMapper.rollbackTest1();
			homeMapper.rollbackTest2();
			platformTransactionManager.commit(status);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);//롤백
		}
		return homeMapper.test(); //오라클은 롤백된 결과를 반환한다.
	}
	
	//롤백 테스트 4 -- MySql 테스트
	public String rollbackTest4(){
		homeMapperMysql = sqlSessionMysql.getMapper(HomeMapperMysql.class);
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			homeMapperMysql.rollbackTestMysql1();//정상 업데이트
			homeMapperMysql.rollbackTestMysql2();//강제 에러 발생
			platformTransactionManager.commit(status);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);//롤백이 안되고 있음
			logger.info(e.getMessage());
		}
		return gson.toJson(homeMapperMysql.testMysql()); //MySql은 롤백된 결과를 반환한다.	
	}
	
	//롤백 테스트 5 -- Oracle, MySql 테스트 : 2개의 DataSource에 대한 rollback이 가능했다.
	public String rollbackTest5(){
		homeMapperMysql = sqlSessionMysql.getMapper(HomeMapperMysql.class);
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			homeMapper.rollbackTest1(); //Oracle 정상 업데이트
			homeMapperMysql.rollbackTestMysql1();//Mysql 정상 업데이트
			homeMapperMysql.rollbackTestMysql2();//강제 에러 발생
			platformTransactionManager.commit(status);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);//롤백
			logger.info(e.getMessage());
		}
		String result = "";
		result += homeMapper.test();
		List<JtMap> r1 =  homeMapperMysql.testMysql();
		JtMap r1_0 = r1.get(0);
		result += r1_0.get("subject");
		return result; //Oracle, MySql 모두 롤백된 결과를 반환한다.			
	}
	
}