package example.group.sd.data.config;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryRDBMS", 
        transactionManagerRef = "transactionManagerRDBMS",
        basePackages = {"example.group.sd.data.repository"})
@EnableTransactionManagement
@PropertySource(value={"classpath:/dev.data.application.properties"}, ignoreResourceNotFound = true)

public class ConfigurationRDBMS {

	@Value("${rdbms.jpa.hibernate.ddl-auto}")
	String jpa_ddl_auto;

	@Value("${rdbms.datasource.manual_configuration}")
	Boolean manual_configuration;

	@Value("${rdbms.datasource.url}")
	String datasource_url;
	@Value("${rdbms.datasource.username}")
	String datasource_username;
	@Value("${rdbms.datasource.password}")
	String datasource_password;
	@Value("${rdbms.datasource.driver-class-name}")
	String datasource_driverClassName;
	@Value("${rdbms.jpa.database-platform}")
	String jpa_dialect;

	public static final String jndi_datasource_name = "java:comp/env/jdbc/scanner-snmp";
	
	static final Logger LOG = LoggerFactory.getLogger(ConfigurationRDBMS.class);

	public ConfigurationRDBMS() {
		LOG.info("constructing ConfigurationRDBMS");
	}

	@Bean(name="dataSourceRDBMS")
	public DataSource dataSource() {		
		LOG.info("calling dataSourceRDBMS");

		DataSource dataSource = null;
		if (manual_configuration) {
			LOG.info("manual datasource configuration: " + datasource_url);
			dataSource = DataSourceBuilder.create()
					.url(datasource_url)
					.username(datasource_username)
					.password(datasource_password)
					.driverClassName(datasource_driverClassName)
					.build();
		} else {
			LOG.info("get datasource from JNDI");
			JndiTemplate jndi = new JndiTemplate();
			try {
				dataSource = (DataSource) jndi.lookup(jndi_datasource_name);
			} catch (NamingException e) {
				LOG.error("NamingException for " + jndi_datasource_name, e);
				throw new RuntimeException(e);
			}
		}
		return dataSource;
	}
	
	@Bean(name="entityManagerFactoryRDBMS")
	public EntityManagerFactory entityManagerFactory() {
		LOG.debug("calling entityManagerFactoryRDBMS");

		//isInitializeLazyStateOutsideTransactionsEnabled()
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
//		vendorAdapter.setDatabasePlatform(jpa_dialect);
		vendorAdapter.setShowSql(false);			//Отображение в консоли запросов, генерируемых hibernate

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("example.group.sd.data.entity");
		factory.setDataSource(dataSource());

//        factory.getJpaPropertyMap().put("hibernate.jdbc.batch_size", 50);
		factory.getJpaPropertyMap().put(AvailableSettings.HBM2DDL_AUTO, jpa_ddl_auto);
//        factory.getJpaPropertyMap().put("hibernate.enable_lazy_load_no_trans", true);
        factory.getJpaPropertyMap().put(AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, true);
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean(name="transactionManagerRDBMS")
	public PlatformTransactionManager transactionManager() {
		LOG.debug("calling transactionManagerRDBMS");

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}	
	
	
	//To resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		LOG.info("propertyConfigInDev create");

		final PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

		propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(true);

		return propertySourcesPlaceholderConfigurer;
	}

	
}
