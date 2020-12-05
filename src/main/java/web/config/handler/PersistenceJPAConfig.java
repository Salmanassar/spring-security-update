package web.config.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "web")
@PropertySource("classpath:application.properties")
public class PersistenceJPAConfig {

        private Environment environment;

        @Autowired
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        @Bean
        public DataSource getDataSource() {
            DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
            managerDataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("database.driver")));
            managerDataSource.setUrl(environment.getProperty("database.url"));
            managerDataSource.setUsername(environment.getProperty("database.username"));
            managerDataSource.setPassword(environment.getProperty("database.password"));
            return managerDataSource;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
            managerFactoryBean.setDataSource(getDataSource());
            managerFactoryBean.setPackagesToScan("web.model");

            JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            managerFactoryBean.setJpaVendorAdapter(adapter);
            Properties properties = new Properties();
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            properties.setProperty("hibernate.hbm2ddl.auto", "create");
            managerFactoryBean.setJpaProperties(properties);

            return managerFactoryBean;
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            JpaTransactionManager manager = new JpaTransactionManager();
            manager.setEntityManagerFactory(entityManagerFactory().getObject());
            return manager;
        }

        @Bean
        public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
            return new PersistenceExceptionTranslationPostProcessor();
        }
    }
