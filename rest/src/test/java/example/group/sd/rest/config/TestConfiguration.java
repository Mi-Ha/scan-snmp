package example.group.sd.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by MNikitin on 15.09.2016.
 */
@Configuration
@Import({example.group.sd.data.config.ConfigurationBase.class})
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
