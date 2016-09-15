package net.rc.lab.springboot.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
public class DatabaseConfiguration implements EnvironmentAware {

  private RelaxedPropertyResolver jpaPropertyResolver;

  @Autowired(required = false)
  private PersistenceUnitManager persistenceUnitManager;

  @Override
  public void setEnvironment(Environment environment) {
    this.jpaPropertyResolver = new RelaxedPropertyResolver(environment, "spring.jpa.");
  }

  /**
   * Configuration to populate the Transaction Manager.
   * @param entityManagerFactory autowired EntityManager
   * @return TransactionManager
   */
  @Bean
  public PlatformTransactionManager transactionManager(
      EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }

  /**
   * Configuration to populate the EntityManager Bean.
   * 
   * @param dataSource
   *          database connection source.
   * @param jpaVendorAdapter
   *          jpa properties reader.
   * @return EntityManagerFactoryBean
   */
  @Bean
  @DependsOn("jdbcTemplate")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
      JpaVendorAdapter jpaVendorAdapter) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
    entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    if (persistenceUnitManager != null) {
      entityManagerFactoryBean.setPersistenceUnitManager(persistenceUnitManager);
    }
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactoryBean.setDataSource(dataSource);
    String packageToScan = jpaPropertyResolver.getProperty("application.packageToScan",
        "net.rc.lab.springboot.entities");
    entityManagerFactoryBean.setPackagesToScan(packageToScan);
    entityManagerFactoryBean.getJpaPropertyMap()
        .putAll(jpaPropertyResolver.getSubProperties("properties."));
    Map<String, Object> properties = entityManagerFactoryBean.getJpaPropertyMap();
    properties.put("hibernate.hbm2ddl.auto",
        jpaPropertyResolver.getProperty("hibernate.ddl-auto", "none"));
    return entityManagerFactoryBean;
  }
}
