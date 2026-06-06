package com.htht.executor.core.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * mybatis-plus 多数据源事务配置
 */

public class MybatisTransactionConfig {

    @Configuration
    @MapperScan(basePackages = "com.htht.executor.product.dao",
            sqlSessionFactoryRef= "mySqlSessionFactory",sqlSessionTemplateRef ="mysqlSessionTemplate")
    public static class MysqlConfig {

        @Autowired
        private GlobalConfig globalConfig;

        @Autowired
        private MybatisConfiguration mybatisConfiguration;

        @Autowired
        private PaginationInterceptor paginationInterceptor;

        @Primary
        @Bean(name = "mySqlSessionFactory")
        public SqlSessionFactory mySqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
            MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
            // 注入分页插件
            bean.setPlugins(new Interceptor[]{paginationInterceptor});
            bean.setDataSource(dataSource);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
            bean.setGlobalConfig(globalConfig);
            bean.setConfiguration(mybatisConfiguration);
            return bean.getObject();
        }


        @Primary
        @Bean(name = "mysqlSessionTemplate")
        public SqlSessionTemplate mysqlSessionTemplate(@Qualifier("mySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    @Configuration
    @MapperScan(basePackages = "com.htht.executor.pqsql.dao",
            sqlSessionFactoryRef= "pgSqlSessionFactory",sqlSessionTemplateRef ="pgsqlSessionTemplate")
    public static class PgSqlConfig {

        @Autowired
        private GlobalConfig globalConfig;

        @Autowired
        private MybatisConfiguration mybatisConfiguration;

        @Autowired
        private PaginationInterceptor paginationInterceptor;

        @Bean(name = "pgSqlSessionFactory")
        public SqlSessionFactory pgSqlSessionFactory(@Qualifier("pgsqlDataSource") DataSource dataSource) throws Exception {
            MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
            // 注入分页插件
            bean.setPlugins(new Interceptor[]{paginationInterceptor});
            bean.setDataSource(dataSource);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/pgsql/**/*.xml"));
            bean.setGlobalConfig(globalConfig);
//            bean.setConfiguration(mybatisConfiguration);
            bean.setDataSource(dataSource);
            return bean.getObject();
        }

        @Bean(name = "pgsqlSessionTemplate")
        public SqlSessionTemplate pgsqlSessionTemplate(@Qualifier("pgSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

}
