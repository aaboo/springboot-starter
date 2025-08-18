package com.nicepay.starter.home;

import java.util.List;

import com.nicepay.starter.common.JtMap;

public interface HomeMapperMysql {
	List<JtMap> testMysql();//MYSQL 테스트
	int rollbackTestMysql1();
	int rollbackTestMysql2();
}
