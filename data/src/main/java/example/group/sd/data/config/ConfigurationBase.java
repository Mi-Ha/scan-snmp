package example.group.sd.data.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;

@Configuration
//@EnableAutoConfiguration
@ComponentScan(basePackages={"example.group.sd.data"})

public class ConfigurationBase {
    static final Logger LOG = LoggerFactory.getLogger(ConfigurationBase.class);

    public ConfigurationBase() {
        LOG.info("constructing ConfigurationBase");
    }

//	public static void main(String[] args) {
//	    SpringApplication.run(SimpleConfiguration.class, args);
//	}
//	
	

	
	
//	@Bean
//	@ConfigurationProperties(prefix="datasource.second")
//	public DataSource secondaryDataSource() {
//	    return DataSourceBuilder.create().build();
//	}
	
}
