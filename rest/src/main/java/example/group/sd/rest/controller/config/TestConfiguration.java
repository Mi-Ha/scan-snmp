package example.group.sd.rest.controller.config;

import example.group.sd.data.config.ConfigurationBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@ComponentScan(basePackages={
		"example.group.sd.rest.data",
		"example.group.sd.rest.controller",
		"example.group.sd.rest.data"
})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Import({ConfigurationBase.class,
		RepositoryRestMvcConfiguration.class, ConfigMVC.class})
public class TestConfiguration extends SpringBootServletInitializer {

	private static final Class<TestConfiguration> applicationClass = TestConfiguration.class;
	private static final Logger log = LoggerFactory.getLogger(applicationClass);

	public static void main(String[] args) {
		SpringApplication.run(applicationClass, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

}