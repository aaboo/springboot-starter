package com.nicepay.starter;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
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
@MapperScan("${server.packagename}")
public class DataSourceConfig {
	
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
		sqlSessionFactoryBean.setConfiguration(getMybatisConfig());
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:**/*Mapper.xml"));
		
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
	/*****************************************************************
	 * Mysql (Not primary - sub)
	 * ***************************************************************/	
	//application.properties	 
	@Value("${datasource.mysql.url}") protected String mysqlUrl;
	@Value("${datasource.mysql.username}") protected String mysqlUsername;
	@Value("${datasource.mysql.password}") protected String mysqlPassword;
	

	@Bean
	public DataSource dataSourceMysql() throws Exception {
		AES256 aes256 = new AES256(tokn);
		HikariDataSource dataSource = new HikariDataSource();
		//dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		dataSource.setJdbcUrl(mysqlUrl);
		dataSource.setUsername(aes256.decode(mysqlUsername));
		dataSource.setPassword(aes256.decode(mysqlPassword));
		dataSource.setAutoCommit(false); //autocommit: false > 이것이 있어야 rollback 기능이 먹힌다.
		//dataSource.setConnectionTestQuery("SELECT 1");
		dataSource.setMaximumPoolSize(50);
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryMysql(@Autowired @Qualifier("dataSourceMysql") DataSource dataSourceMysql) throws Exception {
		logger.info("mysqlSqlSessionFactory() Start");
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSourceMysql);
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		//sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setConfiguration(getMybatisConfig());
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:**/*MapperMysql.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSession sqlSessionMysql(@Autowired @Qualifier("sqlSessionFactoryMysql") SqlSessionFactory sqlSessionFactoryMysql) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryMysql);
	}	
	
	@Bean
	public DataSourceTransactionManager transactionManagerMysql(@Autowired @Qualifier("dataSourceMysql") DataSource dataSourceMysql) {
		return new DataSourceTransactionManager(dataSourceMysql);
	}
	
	/*****************************************************************
	 * MyBatis 설정
	 * ***************************************************************/
	protected org.apache.ibatis.session.Configuration getMybatisConfig() {
		org.apache.ibatis.session.Configuration cfg = new org.apache.ibatis.session.Configuration();
		cfg.setCacheEnabled(true);
		cfg.setLazyLoadingEnabled(false);
		cfg.setAggressiveLazyLoading(false);
		cfg.setMultipleResultSetsEnabled(true);
		cfg.setUseColumnLabel(true);
		cfg.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
		cfg.setDefaultExecutorType(ExecutorType.SIMPLE);
		cfg.setDefaultStatementTimeout(60);
		cfg.setJdbcTypeForNull(JdbcType.VARCHAR);
		cfg.setCallSettersOnNulls(true);
		cfg.setMapUnderscoreToCamelCase(false);
		cfg.setUseGeneratedKeys(false);
		cfg.setLogPrefix("[SQL]");
		return cfg;
	}
}