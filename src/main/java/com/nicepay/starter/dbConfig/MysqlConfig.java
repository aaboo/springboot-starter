package com.nicepay.starter.dbConfig;

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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.nicepay.starter.common.AES256;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value = {"com.nicepay.starter"})
public class MysqlConfig {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${dataSource.tokn}") protected String tokn;
	
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
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/nicepay/starter/**/*MapperMysql.xml"));
		
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
