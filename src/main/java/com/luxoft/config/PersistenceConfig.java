package com.luxoft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;


/**
 * Created by iivaniv on 02.07.2015.
 */
@Configuration
//@EnableTransactionManagement
//@PropertySource({"classpath:app.properties"})
//@EnableJpaRepositories("com.luxoft.persistence.repository")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.luxoft"})
public class PersistenceConfig {

    private static final String PROP_DATABASE_DRIVER = "db.driver";
    private static final String PROP_DATABASE_PASSWORD = "db.password";
    private static final String PROP_DATABASE_URL = "db.url";
    private static final String PROP_DATABASE_USERNAME = "db.username";

    @Resource
    private Environment env;

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
//        dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
//        dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
//        dataSource.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));
//        return dataSource;
//    }

//    @Autowired
//    @Bean
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory, DataSource dataSource) {
//        JpaTransactionManager bean = new JpaTransactionManager(entityManagerFactory);
//        bean.setDataSource(dataSource);
//        return bean;
//    }

//    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

//    @Bean
    public EntityManagerFactory entityManagerFactory() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.INFO);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.luxoft.entity");
//        factory.setDataSource(dataSource());
//        factory.afterPropertiesSet();
        return factory.getObject();
    }


}
