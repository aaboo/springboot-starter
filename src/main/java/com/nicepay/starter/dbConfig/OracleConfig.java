package com.nicepay.starter.dbConfig;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.nicepay.starter.common.AES256;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value = {"com.nicepay.starter"})
public class OracleConfig {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${dataSource.tokn}") protected String tokn;
	
	/*****************************************************************
	 * Oracle (Primay - main)
	 * ***************************************************************/
	//application.properties
	@Value("${datasource.oracle.url}") protected String oracleUrl;
	@Value("${datasource.oracle.username}") protected String oracleUsername;
	@Value("${datasource.oracle.password}") protected String oraclePassword;
	
	@Bean
	@Primary
	public DataSource dataSourceOracle() throws Exception {
		AES256 aes256 = new AES256(tokn);
		HikariDataSource dataSource = new HikariDataSource();
		//dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy"); //로그에 쿼리 출력
		dataSource.setJdbcUrl(oracleUrl);
		dataSource.setUsername(aes256.decode(oracleUsername));
		dataSource.setPassword(aes256.decode(oraclePassword));
		//dataSource.setConnectionTestQuery("SELECT 1 FROM DUAL");
		dataSource.setMaximumPoolSize(50);
		return dataSource;
	}

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactoryOracle(@Autowired @Qualifier("dataSourceOracle") DataSource dataSourceOracle) throws Exception {
		logger.info("sqlSessionFactoryOracle() Start");
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSourceOracle);
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		//sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/nicepay/starter/**/*Mapper.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	@Primary
	public SqlSession sqlSessionOracle(@Autowired @Qualifier("sqlSessionFactoryOracle") SqlSessionFactory sqlSessionFactoryOracle) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryOracle);
	}
	
	@Bean
	@Primary 
	public DataSourceTransactionManager transactionManagerOracle(@Autowired @Qualifier("dataSourceOracle") DataSource dataSourceOracle) {
		return new DataSourceTransactionManager(dataSourceOracle);
	}
}
